package org.poo.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.RoleBasedAccessControl;
import org.poo.SpendingUserInfo;
import org.poo.UserInfo;
import org.poo.accounts.cards.Card;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.visitors.reportVisitors.Visitor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


@Getter
@Setter
public class BusinessAccount extends Account {
    private HashMap<String, ArrayList<Card>> emailToCards;
    private RoleBasedAccessControl rbac;
    private HashMap<String, String> aliasToCommerciantIban;
    private double depositLimit;
    private double spendingLimit;
    private double totalDeposited;
    private double totalSpent;
    private ArrayList<UserInfo> employees;
    private ArrayList<UserInfo> managers;
    private ArrayList<SpendingUserInfo> spendingUserInfos;



    public BusinessAccount(String ownerEmail, String currency) {
        super(currency);
        emailToCards = new HashMap<String, ArrayList<Card>>();
        aliasToCommerciantIban = new HashMap<String, String>();
        emailToCards.put(ownerEmail, new ArrayList<>());
        rbac = new RoleBasedAccessControl(ownerEmail);
        depositLimit = Bank.getInstance().findExchangeRate("RON", getCurrency()) * 500.0;
        spendingLimit = Bank.getInstance().findExchangeRate("RON", getCurrency()) * 500.0;
        totalDeposited = 0;
        totalSpent = 0;
        managers = new ArrayList<UserInfo>();
        employees = new ArrayList<UserInfo>();
        spendingUserInfos = new ArrayList<SpendingUserInfo>();
        setType("business");


    }

    public double getSpendingLimit(String userEmail) {
        if (Bank.getInstance().getUserByEmail(userEmail).getAccounts().contains(this) || rbac.getEmailToRoleMap().get(userEmail).equals("manager")) { // de modificat astea 2 cu manager, faci cu permisiuni
            return 999999999;
        }
        return spendingLimit;
    }

    public double getDepositLimit(String userEmail) {
        if (Bank.getInstance().getUserByEmail(userEmail).getAccounts().contains(this) || rbac.getEmailToRoleMap().get(userEmail).equals("manager")) {
            return 999999999;
        }
        return depositLimit;
    }

    public void addNewBusinessAssociate(String email, String role) {
        if (emailToCards.containsKey(email)) {
            System.out.println("THE EMAIL HAS ALREADY BEEN ADDED! DO NOT READD!");
        } else {
            emailToCards.put(email, new ArrayList<>());
            rbac.addEmail(email, role);
            if(role.equals("employee")) {
                employees.add(new UserInfo(email));
            } else if (role.equals("manager")) {
                managers.add(new UserInfo(email));
            }
        }

    }


    public String getNameByEmail(String email) {
        for (UserInfo userInfo : employees) {
            if (userInfo.getEmail().equals(email)) {
                return userInfo.getUsername();
            }
        }
        for (UserInfo userInfo : managers) {
            if (userInfo.getEmail().equals(email)) {
                return userInfo.getUsername();
            }
        }
        return null;
    }

    public boolean isUserEmployee(String email) {
        for (UserInfo userInfo : employees) {
            if (userInfo.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserManager(String email) {
        for (UserInfo userInfo : managers) {
            if (userInfo.getEmail().equals(email)) {
                return true;
            }

        }
        return false;
    }





    /**
     * iterates through the account cards and returns the corresponding card
     * @param cardNumber the card id
     * @return the Card object corresponding to ID
     */
    public Card getCardByCardNumber(final String cardNumber) {
        for (String email : emailToCards.keySet()) {
            for (Card card : emailToCards.get(email)) {
                if (card.getCardNumber().equals(cardNumber)) {
                    return card;
                }
            }
        }
        throw new CardNotFoundException("");
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

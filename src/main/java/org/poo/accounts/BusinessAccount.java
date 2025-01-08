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

    public void addNewBusinessAssociate(String email, String role) {
        if (emailToCards.containsKey(email)) {
            System.out.println("THE EMAIL HAS ALREADY BEEN ADDED! DO NOT READD!");
            rbac.addEmail(email, role);
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


    public UserInfo getUserInfo(String email) {
        for (UserInfo userInfo : employees) {
            if (userInfo.getEmail().equals(email)) {
                return userInfo;
            }
        }
        for (UserInfo userInfo : managers) {
            if (userInfo.getEmail().equals(email)) {
                return userInfo;
            }
        }
        return null;
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

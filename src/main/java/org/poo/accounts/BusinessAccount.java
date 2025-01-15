package org.poo.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.RoleBasedAccessControl;
import org.poo.SpendingUserInfo;
import org.poo.UserInfo;
import org.poo.accounts.cards.Card;
import org.poo.bankPair.Bank;
import org.poo.exceptions.CardNotFoundException;
import org.poo.visitors.reportVisitors.Visitor;

import java.util.ArrayList;
import java.util.HashMap;


@Getter
@Setter
public final class BusinessAccount extends Account {

    private HashMap<String, ArrayList<Card>> emailToCards;
    private RoleBasedAccessControl rbac;
    private double depositLimit;
    private double spendingLimit;
    private ArrayList<UserInfo> employees;
    private ArrayList<UserInfo> managers;
    private ArrayList<SpendingUserInfo> spendingUserInfos;

    private static final double LIMIT_RON = 500.0;
    private static final double INFINITE = 999999999;



    public BusinessAccount(final String ownerEmail, final String currency) {
        super(currency);
        emailToCards = new HashMap<String, ArrayList<Card>>();
        emailToCards.put(ownerEmail, new ArrayList<>());
        rbac = new RoleBasedAccessControl(ownerEmail);
        depositLimit = Bank.getInstance().findExchangeRate("RON", getCurrency()) * LIMIT_RON;
        spendingLimit = Bank.getInstance().findExchangeRate("RON", getCurrency()) * LIMIT_RON;
        managers = new ArrayList<UserInfo>();
        employees = new ArrayList<UserInfo>();
        spendingUserInfos = new ArrayList<SpendingUserInfo>();
        setType("business");


    }

    /**
     * gets the spending limit of the user given as parameter
     * @param userEmail the user the spending limit is asked for
     * @return the spending limit
     */
    public double getSpendingLimit(final String userEmail) {
        if (Bank.getInstance().getUserByEmail(userEmail).getAccounts().contains(this)
                || rbac.getEmailToRoleMap().get(userEmail).equals("manager")) {
            return INFINITE;
        }
        return spendingLimit;
    }


    /**
     * gets the deposit limit of the user given as parameter
     * @param userEmail the user the spending limit is asked for
     * @return the deposit limit
     */
    public double getDepositLimit(final String userEmail) {
        if (Bank.getInstance().getUserByEmail(userEmail).getAccounts().contains(this)
                || rbac.getEmailToRoleMap().get(userEmail).equals("manager")) {
            return INFINITE;
        }
        return depositLimit;
    }

    /**
     * adds an email to the current business account with the given role
     * @param email the email of the associated user
     * @param role the role of the associated user
     */
    public void addNewBusinessAssociate(final String email, final String role) {
        if (emailToCards.containsKey(email)) {
            return;
        } else {
            emailToCards.put(email, new ArrayList<>());
            rbac.addEmail(email, role);
            if (role.equals("employee")) {
                employees.add(new UserInfo(email));
            } else if (role.equals("manager")) {
                managers.add(new UserInfo(email));
            }
        }

    }



    /**
     * iterates through the list of users and takes returns the name of the user
     * @param email the email of the business associate
     * @return the username
     */
    public String getNameByEmail(final String email) {
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


    /**
     * checks whether a user is employee or not
     * @param email the email of the business associate
     * @return true if the user is an employee and false otherwise
     */
    public boolean isUserEmployee(final String email) {
        for (UserInfo userInfo : employees) {
            if (userInfo.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }


    /**
     * checks whether a user is manager or not
     * @param email the email of the business associate
     * @return true if the user is a manager and false otherwise
     */
    public boolean isUserManager(final String email) {
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


    /**
     * method of accepting the visitor
     * @param visitor the interface of the possible visitor classes
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}

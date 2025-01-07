package org.poo.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.RoleBasedAccessControl;
import org.poo.accounts.cards.Card;
import org.poo.bankPair.Bank;
import org.poo.exceptions.CardNotFoundException;
import org.poo.visitors.reportVisitors.Visitor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


@Getter
@Setter
public class BusinessAccount extends Account {
    HashMap<String, ArrayList<Card>> emailToCards;
    RoleBasedAccessControl rbac;
    HashMap<String, String> aliasToCommerciantIban;

    public BusinessAccount(String ownerEmail, String currency) {
        super(currency);
        emailToCards = new HashMap<String, ArrayList<Card>>();
        aliasToCommerciantIban = new HashMap<String, String>();
        emailToCards.put(ownerEmail, new ArrayList<>());
        rbac = new RoleBasedAccessControl(ownerEmail, 500.0 * Bank.getInstance().findExchangeRate("RON", getCurrency()));


    }

    public void addNewBusinessAssociate(String email, String role) {
        if (emailToCards.containsKey(email)) {
            System.out.println("THE EMAIL HAS ALREADY BEEN ADDED! DO NOT READD!");
            rbac.addEmail(email, role, 500.0 * Bank.getInstance().findExchangeRate("RON", getCurrency()));
        } else {
            emailToCards.put(email, new ArrayList<>());
            rbac.addEmail(email, role, 500.0 * Bank.getInstance().findExchangeRate("RON", getCurrency()));
        }

    }

    public void changeDepositLimit(double newLimit, String email) {
        if (emailToCards.containsKey(email)) {
            rbac.getEmailToDepositLimitMap().put(email, newLimit);
        }
    }

    public void changeSpendingLimit(double newLimit, String email) {
        if (emailToCards.containsKey(email)) {
            rbac.getEmailToSpendingLimitMap().put(email, newLimit);
        }
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

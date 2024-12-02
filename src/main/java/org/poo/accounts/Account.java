package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.PayOnlineVisitor;
import org.poo.cards.Card;

import java.util.ArrayList;

@Getter
@Setter

public abstract class Account {
    // poate clasa abstracta
    private String currency;
    private double minBalance = 0;
    private String alias;
    private String IBAN;
    private ArrayList<Card> cards;
    private double balance;
    private String type;


    public Card getCardByCardNumber(String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }


}

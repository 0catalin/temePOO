package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.cards.Card;

import java.util.ArrayList;

@Getter
@Setter

public abstract class Account {
    // poate clasa abstracta
    private String currency;
    private double minBalance = -1; // poate 0
    private String alias;
    private String IBAN;
    private ArrayList<Card> cards;
    private double balance;
    private String type;


}

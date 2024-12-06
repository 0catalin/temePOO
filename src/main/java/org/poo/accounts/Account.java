package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.PayOnlineVisitor;
import org.poo.ReportVisitor;
import org.poo.SpendingsReportVisitor;
import org.poo.cards.Card;
import org.poo.commands.Report;

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
    private ArrayList<ObjectNode> spendingReports;
    private ArrayList<ObjectNode> reportsSavings;
    private ArrayList<ObjectNode> reportsClassic;


    public Card getCardByCardNumber(String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    public void checkAccountCards() {
        if (balance < minBalance && cards != null) {
            for (Card card : cards) {
                if (!card.getStatus().equals("blocked")) {
                    card.setStatus("frozen");
                }
            }
        }

        else if (balance > minBalance && cards != null) {
            for (Card card : cards) {
                if (card.getStatus().equals("frozen")) {
                    card.setStatus("active");
                }
            }
        }
    }

    public abstract void accept(SpendingsReportVisitor visitor);
    public abstract void accept(ReportVisitor visitor);

}

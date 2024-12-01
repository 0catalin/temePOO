package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.OneTimeCard;
import org.poo.cards.RegularCard;

public class PayOnlineVisitor {
    private double amount;
    private int timestamp;
    private String description;
    private String commerciant;
    private ObjectMapper mapper;
    private ArrayNode output;
    private Account account;

    public PayOnlineVisitor(double amount, int timestamp, String description,
                            String commerciant, ObjectMapper mapper, ArrayNode output, Account account) {

        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
        this.commerciant = commerciant;
        this.mapper = mapper;
        this.output = output;
        this.account = account;
    }

    public void visit(OneTimeCard card) {
        if (account.getBalance() < amount) {
            System.out.println("Insufficient funds");
        }
        else {

            account.setBalance(account.getBalance() - amount);
            card.setStatus("blocked");
        }
    }

    public void visit(RegularCard card) {
        if (account.getBalance() < amount) {
            System.out.println("Insufficient funds");
        } else {

            account.setBalance(account.getBalance() - amount);
        }
    }

}

package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class PayOnline implements Command{
    private String cardNumber;
    private double amount;
    private int timestamp;
    private String currency;
    private String description;
    private String commerciant;
    private String email;

    public PayOnline(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        amount = commandInput.getAmount();
        commerciant = commandInput.getCommerciant();
        description = commandInput.getDescription();
        cardNumber = commandInput.getCardNumber();
    }

    public void execute(Bank bank, ArrayNode output) {

    }
}

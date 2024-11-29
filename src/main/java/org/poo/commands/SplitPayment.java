package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

import java.util.List;

public class SplitPayment implements Command{
    private double amount;
    private int timestamp;
    private String currency;
    List<String> accountsForSplit;

    public SplitPayment(CommandInput commandInput) {
        amount = commandInput.getAmount();
        timestamp = commandInput.getTimestamp();
        currency = commandInput.getCurrency();
        accountsForSplit = commandInput.getAccounts();
    }

    public void execute(Bank bank, ArrayNode output) {

    }
}
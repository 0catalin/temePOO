package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class AddFunds implements Command{
    private String account;
    private double amount;
    private int timestamp;

    public AddFunds(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        account = commandInput.getAccount();
    }

    public void execute(Bank bank, ArrayNode output) {

    }
}
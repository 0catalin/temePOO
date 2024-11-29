package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class AddSavingsAccount implements Command{
    private String email;
    private String currency;
    private int timestamp;
    private double interestRate;
    public AddSavingsAccount(CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
        interestRate = commandInput.getInterestRate();
    }
    public void execute(Bank bank, ArrayNode output) {

    }
}

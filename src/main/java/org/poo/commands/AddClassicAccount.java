package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class AddClassicAccount implements Command{
    private String email;
    private String currency;
    private int timestamp;
    public AddClassicAccount(CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
    }
    public void execute(Bank bank, ArrayNode output) {

    }
}

package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class CreateOneTimeCard implements Command{
    private String account;
    private String email;
    private int timestamp;

    public CreateOneTimeCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        account = commandInput.getAccount();
    }

    public void execute(Bank bank, ArrayNode output) {

    }
}
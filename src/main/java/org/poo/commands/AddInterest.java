package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

public class AddInterest implements Command{
    private String account;
    private int timestamp;

    public AddInterest(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        account = commandInput.getAccount();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {

    }
}

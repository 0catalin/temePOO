package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class PrintTransactions implements Command{
    private String email;
    private int timestamp;
    public PrintTransactions(CommandInput commandInput) {
        email = commandInput.getEmail();
        timestamp = commandInput.getTimestamp();
    }

    @Override
    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {

    }
}

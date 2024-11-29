package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class PrintUsers implements Command{
    private int timestamp;
    public PrintUsers(CommandInput input) {
        timestamp = input.getTimestamp();
    }
    @Override
    public void execute(Bank bank, ArrayNode output) {

    }
}

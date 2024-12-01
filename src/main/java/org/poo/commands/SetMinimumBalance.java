package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

public class SetMinimumBalance implements Command{
    private String account;
    private double minBalance;
    private int timestamp;

    public SetMinimumBalance(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        account = commandInput.getAccount();
        minBalance = commandInput.getMinBalance();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {

    }
}
package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

public class Report implements Command{
    private int startTimestamp;
    private int endTimestamp;
    private String account;

    public Report(CommandInput commandInput) {
        account = commandInput.getAccount();
        endTimestamp = commandInput.getEndTimestamp();
        startTimestamp = commandInput.getStartTimestamp();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {

    }

}

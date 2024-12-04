package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.ReportVisitor;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Report implements Command{
    private int startTimestamp;
    private int endTimestamp;
    private String IBAN;
    private int timestamp;

    public Report(CommandInput commandInput) {
        IBAN = commandInput.getAccount();
        endTimestamp = commandInput.getEndTimestamp();
        startTimestamp = commandInput.getStartTimestamp();
        timestamp = commandInput.getTimestamp();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        User user = bank.getUserByIBAN(IBAN);
        if(user != null) {
            ReportVisitor visitor = new ReportVisitor(user, mapper, startTimestamp, endTimestamp, timestamp, output, IBAN, bank);
            account.accept(visitor);
        } else {
            userNotFound(output, mapper);
        }
    }


    private void userNotFound(ArrayNode output, ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "report");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", "Account not found");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}

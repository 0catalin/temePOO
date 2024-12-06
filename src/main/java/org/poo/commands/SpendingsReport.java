package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.SpendingsReportVisitor;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpendingsReport implements Command{
    private int startTimestamp;
    private int endTimestamp;
    private String IBAN;
    private int timestamp;

    public SpendingsReport(CommandInput commandInput) {
        IBAN = commandInput.getAccount();
        endTimestamp = commandInput.getEndTimestamp();
        startTimestamp = commandInput.getStartTimestamp();
        timestamp = commandInput.getTimestamp();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        User user = Bank.getInstance().getUserByIBAN(IBAN);
        if(user != null) {
            SpendingsReportVisitor visitor = new SpendingsReportVisitor(IBAN, timestamp,
                    startTimestamp, endTimestamp);
            account.accept(visitor);
        } else {
            userNotFound(Bank.getInstance().getOutput());
        }
    }

    private void userNotFound(ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "spendingsReport");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", "Account not found");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}

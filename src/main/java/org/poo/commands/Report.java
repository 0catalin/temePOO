package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.visitors.reportVisitors.ReportVisitor;
import org.poo.visitors.reportVisitors.Visitor;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

public final class Report implements Command {
    private int startTimestamp;
    private int endTimestamp;
    private String iban;
    private int timestamp;

    public Report(final CommandInput commandInput) {
        iban = commandInput.getAccount();
        endTimestamp = commandInput.getEndTimestamp();
        startTimestamp = commandInput.getStartTimestamp();
        timestamp = commandInput.getTimestamp();
    }

    @Override
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        User user = Bank.getInstance().getUserByIBAN(iban);
        if (user != null) {
            Visitor visitor = new ReportVisitor(startTimestamp, endTimestamp, timestamp, iban);
            account.accept(visitor);
        } else {
            userNotFound(Bank.getInstance().getOutput());
        }
    }


    private void userNotFound(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
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

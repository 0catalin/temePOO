package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.exceptions.UserNotFoundException;
import org.poo.visitors.reportVisitors.SpendingsReportVisitor;
import org.poo.visitors.reportVisitors.Visitor;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

/**
 * class implementing the spendings report command
 */
public final class SpendingsReport implements Command {
    private int startTimestamp;
    private int endTimestamp;
    private String iban;
    private int timestamp;

    public SpendingsReport(final CommandInput commandInput) {
        iban = commandInput.getAccount();
        endTimestamp = commandInput.getEndTimestamp();
        startTimestamp = commandInput.getStartTimestamp();
        timestamp = commandInput.getTimestamp();
    }

    @Override
    public void execute() {
        try {
            User user = Bank.getInstance().getUserByIBAN(iban);
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            Visitor visitor = new SpendingsReportVisitor(iban, timestamp,
                        startTimestamp, endTimestamp);
            account.accept(visitor);
        } catch (UserNotFoundException e) {
            userNotFound(Bank.getInstance().getOutput());
        }
    }

    private void userNotFound(final ArrayNode output) {
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

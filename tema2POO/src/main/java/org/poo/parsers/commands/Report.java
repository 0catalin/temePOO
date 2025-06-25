package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.visitors.accountVisitors.reportVisitors.ReportVisitor;
import org.poo.visitors.accountVisitors.Visitor;
import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the report command
 */

public final class Report implements Command {

    private final int startTimestamp;
    private final int endTimestamp;
    private final String iban;
    private final int timestamp;


    public Report(final CommandInput commandInput) {
        iban = commandInput.getAccount();
        endTimestamp = commandInput.getEndTimestamp();
        startTimestamp = commandInput.getStartTimestamp();
        timestamp = commandInput.getTimestamp();
    }



    /**
     * if the account is found a visitor is initialized and the account accepts it
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            Visitor visitor = new ReportVisitor(startTimestamp, endTimestamp, timestamp, iban);
            account.accept(visitor);
        } catch (AccountNotFoundException e) {
            accountNotFound(Bank.getInstance().getOutput());
        }
    }



    private void accountNotFound(final ArrayNode output) {
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

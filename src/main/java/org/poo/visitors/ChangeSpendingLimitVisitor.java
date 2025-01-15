package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.visitors.reportVisitors.Visitor;

public final class ChangeSpendingLimitVisitor implements Visitor {

    private final double amount;
    private final String email;
    private final int timestamp;

    public ChangeSpendingLimitVisitor(final double amount, final String email, final int timestamp) {
        this.amount = amount;
        this.email = email;
        this.timestamp = timestamp;
    }

    public void visit(final ClassicAccount account) {
        Bank.getInstance().getOutput().add(notBusinessAccount());
    }


    public void visit(final SavingsAccount account) {
        Bank.getInstance().getOutput().add(notBusinessAccount());
    }


    public void visit(final BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {
            return;
        } else if (!account.getRbac().hasPermissions(email, "changeSpendingLimit")) {
            Bank.getInstance().getOutput().add(lackOfPermissions());
        } else if (amount < 0) {
            return;
        } else {
            account.setSpendingLimit(amount);
        }
    }

    private ObjectNode lackOfPermissions() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "changeSpendingLimit");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description",
                "You must be owner in order to change spending limit.");
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }

    private ObjectNode notBusinessAccount() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "changeSpendingLimit");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "This is not a business account");
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }
}

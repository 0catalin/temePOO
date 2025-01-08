package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.visitors.reportVisitors.Visitor;

public class ChangeSpendingLimitVisitor implements Visitor {

    private double amount;
    private String email;
    private int timestamp;

    public ChangeSpendingLimitVisitor(double amount, String email, int timestamp) {
        this.amount = amount;
        this.email = email;
        this.timestamp = timestamp;
    }

    public void visit(ClassicAccount account) {

    }


    public void visit(SavingsAccount account) {

    }


    public void visit(BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getRbac().hasPermissions(email, "changeSpendingLimit")) {
            Bank.getInstance().getOutput().add(lackOfPermissions());
        } else if (amount < 0) {

        } else {
            account.setSpendingLimit(amount);
        }
    }

    private ObjectNode lackOfPermissions() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "changeSpendingLimit");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "You must be owner in order to change spending limit.");
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }
}

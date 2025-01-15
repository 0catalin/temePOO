package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.visitors.reportVisitors.Visitor;

public class ChangeDepositLimitVisitor implements Visitor {
    private int timestamp;
    private double amount;
    private String email;

    public ChangeDepositLimitVisitor(int timestamp, double amount, String email) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.email = email;
    }

    @Override
    public void visit (ClassicAccount account) {

    }

    @Override
    public void visit (SavingsAccount account) {

    }

    @Override
    public void visit(BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getRbac().hasPermissions(email, "changeDepositLimit")) {
            Bank.getInstance().getOutput().add(lackOfPermissions());
        } else if (amount < 0) {

        } else {
            account.setDepositLimit(amount);
        }
    }

    private ObjectNode lackOfPermissions() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "changeDepositLimit");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "You must be owner in order to change deposit limit.");
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }
}

package org.poo.visitors.accountVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.poo.accounts.business.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;


/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class ChangeDepositLimitVisitor implements Visitor {
    private final int timestamp;
    private final double amount;
    private final String email;

    public ChangeDepositLimitVisitor(final int timestamp, final double amount, final String email) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.email = email;
    }

    /**
     * empty function, will add error when the format is out
     * @param account the classic account
     */
    @Override
    public void visit(final ClassicAccount account) {

    }

    /**
     * empty function, will add error when the format is out
     * @param account the savings account
     */
    @Override
    public void visit(final SavingsAccount account) {

    }


    /**
     * changes the business account field only if the email has permissions
     * @param account the business account
     */
    @Override
    public void visit(final BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {
            return;
        } else if (!account.getRbac().hasPermissions(email, "changeDepositLimit")) {
            Bank.getInstance().getOutput().add(lackOfPermissions());
        } else if (amount < 0) {
            return;
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

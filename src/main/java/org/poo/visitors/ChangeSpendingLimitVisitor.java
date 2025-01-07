package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public class ChangeSpendingLimitVisitor implements Visitor {

    private double amount;
    private String email;

    public ChangeSpendingLimitVisitor(double amount, String email) {
        this.amount = amount;
        this.email = email;
    }

    public void visit(ClassicAccount account) {

    }


    public void visit(SavingsAccount account) {

    }


    public void visit(BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getRbac().hasPermissions(email, "changeSpendingLimit")) {

        } else if (amount < 0) {

        } else {
            account.setSpendingLimit(amount);
        }
    }
}

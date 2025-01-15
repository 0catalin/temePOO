package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public final class SetMinBalanceVisitor implements Visitor {
    private final double minBalance;
    private final int timestamp;

    public SetMinBalanceVisitor(final double minBalance, final int timestamp) {
        this.minBalance = minBalance;
        this.timestamp = timestamp;
    }




    public void visit(final ClassicAccount account) {
        account.setMinBalance(minBalance);
    }



    public void visit(final BusinessAccount account) {
        // should be given email in input to check for owner
        account.setMinBalance(minBalance);
    }



    public void visit(final SavingsAccount account) {
        account.setMinBalance(minBalance);
    }
}

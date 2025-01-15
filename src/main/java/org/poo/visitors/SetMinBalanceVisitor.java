package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public class SetMinBalanceVisitor implements Visitor {
    private double minBalance;
    private int timestamp;

    public SetMinBalanceVisitor(double minBalance, int timestamp) {
        this.minBalance = minBalance;
        this.timestamp = timestamp;
    }




    public void visit(ClassicAccount account) {
        account.setMinBalance(minBalance);
    }



    public void visit(BusinessAccount account) {
        // should be given email in input to check for owner
        account.setMinBalance(minBalance);
    }



    public void visit(SavingsAccount account) {
        account.setMinBalance(minBalance);
    }
}

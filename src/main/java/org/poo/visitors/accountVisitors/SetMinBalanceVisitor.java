package org.poo.visitors.accountVisitors;

import org.poo.accounts.business.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;


/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class SetMinBalanceVisitor implements Visitor {
    private final double minBalance;

    public SetMinBalanceVisitor(final double minBalance) {
        this.minBalance = minBalance;
    }


    /**
     * sets the min balance
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {
        account.setMinBalance(minBalance);
    }


    /**
     * sets the min balance
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {
        // should be given email in input to check for owner
        account.setMinBalance(minBalance);
    }


    /**
     * sets the min balance
     * @param account the savings account
     */
    public void visit(final SavingsAccount account) {
        account.setMinBalance(minBalance);
    }
}

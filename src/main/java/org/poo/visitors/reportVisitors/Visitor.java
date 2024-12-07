package org.poo.visitors.reportVisitors;

import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;

/**
 * interface used for the Visitors who do operations on accounts
 */
public interface Visitor {
    /**
     * method which performs an operation on a savings account
     * @param account the savings account
     */
    void visit(SavingsAccount account);

    /**
     * method which performs an operation on a classic account
     * @param account the classic account
     */
    void visit(ClassicAccount account);
}

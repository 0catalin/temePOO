package org.poo.visitors.accountVisitors;

import org.poo.accounts.business.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;


/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class AddNewBusinessAssociateVisitor implements Visitor {

    private final String role;
    private final String email;


    public AddNewBusinessAssociateVisitor(final String email, final String role) {
        this.email = email;
        this.role = role;
    }

    /**
     * empty method for savings account
     * @param account the savings account
     */
    @Override
    public void visit(final SavingsAccount account) {

    }

    /**
     * adds new business associate to the account
     * @param account the business account
     */
    @Override
    public void visit(final BusinessAccount account) {
        account.addNewBusinessAssociate(email, role);
    }

    /**
     * empty method for classic account
     * @param account the classic account
     */
    @Override
    public void visit(final ClassicAccount account) {

    }
}

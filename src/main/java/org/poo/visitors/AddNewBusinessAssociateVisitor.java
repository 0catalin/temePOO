package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;


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


    @Override
    public void visit(final SavingsAccount account) {

    }


    @Override
    public void visit(final BusinessAccount account) {
        account.addNewBusinessAssociate(email, role);
    }


    @Override
    public void visit(final ClassicAccount account) {

    }
}

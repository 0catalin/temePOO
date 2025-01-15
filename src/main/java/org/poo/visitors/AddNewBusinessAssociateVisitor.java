package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public class AddNewBusinessAssociateVisitor implements Visitor {

    private String role;
    private String email;


    public AddNewBusinessAssociateVisitor(String email, String role) {
        this.email = email;
        this.role = role;
    }


    @Override
    public void visit(SavingsAccount account) {

    }


    @Override
    public void visit(BusinessAccount account) {
        account.addNewBusinessAssociate(email, role);
    }


    @Override
    public void visit(ClassicAccount account) {

    }
}

package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public class SendMoneyVisitor implements Visitor {



    public void visit(ClassicAccount account) {

    }



    public void visit(BusinessAccount account) {

    }



    public void visit(SavingsAccount account) {

    }
}

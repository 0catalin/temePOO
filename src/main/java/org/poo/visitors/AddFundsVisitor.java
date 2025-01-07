package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public class AddFundsVisitor implements Visitor {

    private String email;
    private double amount;

    public AddFundsVisitor(double amount, String email) {
        this.amount = amount;
        this.email = email;
    }



    public void visit(ClassicAccount account) {
        account.setBalance(account.getBalance() + amount);
    }



    public void visit(BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getRbac().hasPermissions(email, "addFunds")) {

        } else if (account.getRbac().getEmailToDepositLimitMap().get(email) < amount) {

        } else {
            account.setBalance(account.getBalance() + amount);
        }
    }



    public void visit(SavingsAccount account) {
        account.setBalance(account.getBalance() + amount);
    }
}

package org.poo.visitors;

import org.poo.SpendingUserInfo;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public class AddFundsVisitor implements Visitor {

    private String email;
    private double amount;
    private int timestamp;

    public AddFundsVisitor(double amount, String email, int timestamp) {
        this.amount = amount;
        this.email = email;
        this.timestamp = timestamp;
    }



    public void visit(ClassicAccount account) {
        account.setBalance(account.getBalance() + amount);
    }



    public void visit(BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getRbac().hasPermissions(email, "addFunds")) {

        } else if (account.getDepositLimit() > amount) {

        } else {
            account.setBalance(account.getBalance() + amount);

            account.getSpendingUserInfos().add(new SpendingUserInfo(amount, 0, email, timestamp));
        }
    }



    public void visit(SavingsAccount account) {
        account.setBalance(account.getBalance() + amount);
    }
}

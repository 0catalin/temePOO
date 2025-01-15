package org.poo.visitors;


import org.poo.SpendingUserInfoBuilder;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;

public final class AddFundsVisitor implements Visitor {

    private final String email;
    private final double amount;
    private final int timestamp;

    public AddFundsVisitor(final double amount, final String email, final int timestamp) {
        this.amount = amount;
        this.email = email;
        this.timestamp = timestamp;
    }



    public void visit(final ClassicAccount account) {
        account.setBalance(account.getBalance() + amount);
    }



    public void visit(final BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {
            return;
        } else if (!account.getRbac().hasPermissions(email, "addFunds")) {
            return;
        } else if (account.getDepositLimit(email) < amount) {
            return;
        } else {
            account.setBalance(account.getBalance() + amount);
            account.getSpendingUserInfos().add(new SpendingUserInfoBuilder(email, timestamp)
                    .deposited(amount).build());
        }
    }



    public void visit(final SavingsAccount account) {
        account.setBalance(account.getBalance() + amount);
    }
}

package org.poo.visitors;


import org.poo.SpendingUserInfoBuilder;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.visitors.reportVisitors.Visitor;


/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class AddFundsVisitor implements Visitor {

    private final String email;
    private final double amount;
    private final int timestamp;

    public AddFundsVisitor(final double amount, final String email, final int timestamp) {
        this.amount = amount;
        this.email = email;
        this.timestamp = timestamp;
    }


    /**
     * method that adds funds to the account
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {
        account.setBalance(account.getBalance() + amount);
    }



    /**
     * method that adds funds to the account after doing checkings
     * if the email has the permissions
     * @param account the classic account
     */
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




    /**
     * method that adds funds to the account
     * @param account the classic account
     */
    public void visit(final SavingsAccount account) {
        account.setBalance(account.getBalance() + amount);
    }
}

package org.poo.strategies;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;

/**
 * class designed for the spending threshold cashback method
 */
public final class SpendingThresholdStrategy implements Strategy {

    private final Account account;
    private final double amount;


    public SpendingThresholdStrategy(final Account account, final double amount) {
        this.account = account;
        this.amount = amount;
    }


    /**
     * method which adds the amount spent so far to the variable storing it
     */
    public void execute() {
        // increases spent amount so far by the given amount
        account.getDiscountInfo().setSpendingThreshold(account
                .getDiscountInfo().getSpendingThreshold()
                + amount * Bank.getInstance()
                .findExchangeRate(account.getCurrency(), "RON"));
    }

}

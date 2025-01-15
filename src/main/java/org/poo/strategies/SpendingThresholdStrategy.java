package org.poo.strategies;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;


public final class SpendingThresholdStrategy implements Strategy {

    private final Account account;
    private final double amount;


    public SpendingThresholdStrategy(final Account account, final double amount) {
        this.account = account;
        this.amount = amount;
    }


    public void execute() {
        account.getDiscountInfo().setSpendingThreshold(account
                .getDiscountInfo().getSpendingThreshold()
                + amount * Bank.getInstance()
                .findExchangeRate(account.getCurrency(), "RON"));
    }

}

package org.poo.strategies;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;


public class SpendingThresholdStrategy implements Strategy {
    private Account account;
    private double amount;
    public SpendingThresholdStrategy(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }
    public void execute() {
        account.getDiscountInfo().setSpendingThreshold(account.getDiscountInfo().getSpendingThreshold()
                + amount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"));
    }
}

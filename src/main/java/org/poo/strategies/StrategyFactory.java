package org.poo.strategies;

import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;

public abstract class StrategyFactory {
    public static Strategy createStrategy(final Commerciant commerciant,
                                          final Account account, final double amount) {
        if (commerciant.getCashbackStrategy().equals("spendingThreshold")) {
            return new SpendingThresholdStrategy(account, amount);
        }

        if (commerciant.getCashbackStrategy().equals("nrOfTransactions")) {
            return new NrOfTransactionsStrategy(account, commerciant);
        }

        return null; // de facut exceptie aici
    }
}

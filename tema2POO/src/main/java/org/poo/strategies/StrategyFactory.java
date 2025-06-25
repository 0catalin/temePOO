package org.poo.strategies;

import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;
import org.poo.exceptions.StrategyNotFoundException;


/**
 * class with just 1 static function designed to create Strategy instances
 * based on given type
 */
public abstract class StrategyFactory {

    /**
     * creates a strategy instance based on the commerciant given
     * @param commerciant the commerciant the strategy is created based on
     * @param account the account of the user
     * @param amount the amount the user will spend
     * @return the strategy depending on the commerciant
     */
    public static Strategy createStrategy(final Commerciant commerciant,
                                          final Account account, final double amount) {
        if (commerciant.getCashbackStrategy().equals("spendingThreshold")) {
            return new SpendingThresholdStrategy(account, amount);
        }

        if (commerciant.getCashbackStrategy().equals("nrOfTransactions")) {
            return new NrOfTransactionsStrategy(account, commerciant);
        }

        throw new StrategyNotFoundException("");
    }
}

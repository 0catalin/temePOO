package org.poo.strategies;

import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;


/**
 * class designed for the numberoftransactions cashback method
 */
public final class NrOfTransactionsStrategy implements Strategy {

    private final Account account;
    private final Commerciant commerciant;

    private static final int FOOD_TRANSACTIONS = 2;
    private static final int CLOTHES_TRANSACTIONS = 5;
    private static final int TECH_TRANSACTIONS = 10;


    public NrOfTransactionsStrategy(final Account account, final Commerciant commerciant) {
        this.account = account;
        this.commerciant = commerciant;
    }


    /**
     * method which adds payments for commerciants and if the target number is reached
     * the cashback is received by the user
     */
    public void execute() {
        // adds 1 to the hashMap between commerciants and the number of payments for each
        account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant()
                .put(commerciant.getCommerciant(),
                account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant()
                        .getOrDefault(commerciant.getCommerciant(), 0) + 1);

        // checks if a coupon should be received and if it should
        // give it (the boolean)
        if (account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant()
                .get(commerciant.getCommerciant()) == FOOD_TRANSACTIONS
                && !account.getDiscountInfo()
                .isHasHadFoodCashback()) {
            account.getDiscountInfo().setFoodCashback(true);
            account.getDiscountInfo().setHasHadFoodCashback(true);

        } else if (account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant()
                .get(commerciant.getCommerciant()) == CLOTHES_TRANSACTIONS
                && !account.getDiscountInfo()
                .isHasHadClothesCashback()) {
            account.getDiscountInfo().setClothesCashback(true);
            account.getDiscountInfo().setHasHadClothesCashback(true);

        } else if (account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant()
                .get(commerciant.getCommerciant()) == TECH_TRANSACTIONS
                && !account.getDiscountInfo()
                .isHasHadTechCashback()) {
            account.getDiscountInfo().setTechCashback(true);
            account.getDiscountInfo().setHasHadTechCashback(true);
        }


    }

}

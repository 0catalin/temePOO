package org.poo.strategies;

import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;

public class NrOfTransactionsStrategy implements Strategy {
    private Account account;
    private Commerciant commerciant;
    public NrOfTransactionsStrategy(Account account, Commerciant commerciant) {
        this.account = account;
        this.commerciant = commerciant;
    }

    public void execute() {
        account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant().put(commerciant.getCommerciant(),
                account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant().getOrDefault(commerciant.getCommerciant(), 0) + 1);
        if (account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant().get(commerciant.getCommerciant()) == 2 && !account.getDiscountInfo().isHasHadFoodCashback()) {
            account.getDiscountInfo().setFoodCashback(true);
            account.getDiscountInfo().setHasHadFoodCashback(true);
        } else if (account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant().get(commerciant.getCommerciant()) == 5 && !account.getDiscountInfo().isHasHadClothesCashback()) {
            account.getDiscountInfo().setClothesCashback(true);
            account.getDiscountInfo().setHasHadClothesCashback(true);
        } else if (account.getDiscountInfo().getNumberOfTransactionsForEachCommerciant().get(commerciant.getCommerciant()) == 10 && !account.getDiscountInfo().isHasHadTechCashback()) {
            account.getDiscountInfo().setTechCashback(true);
            account.getDiscountInfo().setHasHadTechCashback(true);
        }


    }

}

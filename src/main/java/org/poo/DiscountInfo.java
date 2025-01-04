package org.poo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class DiscountInfo {
    private boolean foodCashback;
    private boolean clothesCashback;
    private boolean techCashback;
    private boolean hasHadFoodCashback;
    private boolean hasHadClothesCashback;
    private boolean hasHadTechCashback;
    private double spendingThreshold;
    private int nrOfTransactions;
    private HashMap<String, Integer> numberOfTransactionsForEachCommerciant;

    public DiscountInfo() {
        foodCashback = false;
        clothesCashback = false;
        techCashback = false;
        hasHadFoodCashback = false;
        hasHadClothesCashback = false;
        hasHadTechCashback = false;
        spendingThreshold = 0;
        nrOfTransactions = 0;
        numberOfTransactionsForEachCommerciant = new HashMap<String, Integer>();
    }

    public int totalTransactions() {
        int total = 0;
        for (Integer value : numberOfTransactionsForEachCommerciant.values()) {
            total += value;
        }
        return total;
    }
}

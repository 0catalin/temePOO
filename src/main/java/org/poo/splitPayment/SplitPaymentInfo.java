package org.poo.splitPayment;

import org.poo.PayAllObserver;
import org.poo.parsers.fileio.CommandInput;

import java.util.List;

public class SplitPaymentInfo {
    private double amount;
    private int timestamp;
    private String currency;
    private List<String> accountsForSplit;
    private PayAllObserver observer;
    private String splitPaymentType;

    public SplitPaymentInfo(CommandInput commandInput) {
        amount = commandInput.getAmount();
        timestamp = commandInput.getTimestamp();
        currency = commandInput.getCurrency();
        accountsForSplit = commandInput.getAccounts();
        observer = new PayAllObserver(accountsForSplit);
        splitPaymentType = commandInput.getSplitPaymentType();
    }

}

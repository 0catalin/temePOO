package org.poo.parsers.commands;

import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;
import org.poo.splitPayment.SplitPaymentInfoNonEqual;

import java.util.ArrayList;
import java.util.List;

public class CustomSplitPayment implements Command {
    //private ArrayList<Double> amountsForUsers;
    //private double amount;
    //private int timestamp;
    //private String currency;
    //private List<String> accountsForSplit;
    CommandInput input;


    public CustomSplitPayment(CommandInput commandInput) {
        //amount = commandInput.getAmount();
        //timestamp = commandInput.getTimestamp();
        //currency = commandInput.getCurrency();
        //accountsForSplit = commandInput.getAccounts();
        //amountsForUsers = (ArrayList<Double>) commandInput.getAmountForUsers();
        input = commandInput;
    }

    public void execute() {
        Bank.getInstance().getSplitPayments().add(new SplitPaymentInfoNonEqual(input));
    }
}

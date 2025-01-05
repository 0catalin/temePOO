package org.poo.splitPayment;

import org.poo.parsers.fileio.CommandInput;

import java.util.ArrayList;

public class SplitPaymentInfoNonEqual extends SplitPaymentInfo {
    private ArrayList<Double> amountsForUsers;
    public SplitPaymentInfoNonEqual(CommandInput commandInput) {
        super(commandInput);
        amountsForUsers = (ArrayList<Double>) commandInput.getAmountForUsers();
    }
}

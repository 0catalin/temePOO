package org.poo.parsers.commands;

import org.poo.bankPair.Bank;
import org.poo.exceptions.PaymentInfoNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;

public class AcceptSplitPayment implements Command {

    private String email;
    private String splitPaymentType;
    private int timestamp;


    public AcceptSplitPayment(CommandInput commandInput) {
        email = commandInput.getEmail();
        splitPaymentType = commandInput.getSplitPaymentType();
        timestamp = commandInput.getTimestamp();
    }


    public void execute() {
        try {
            SplitPaymentInfo splitPaymentInfo = Bank.getInstance().getSplitPaymentByTypeAndEmail(email, splitPaymentType);
            if (splitPaymentInfo.getObserver().update(email)) {
                Bank.getInstance().getSplitPayments().remove(splitPaymentInfo);
                splitPaymentInfo.successfulPayment();
            }
        } catch (PaymentInfoNotFoundException ignored) {

        }
    }
}

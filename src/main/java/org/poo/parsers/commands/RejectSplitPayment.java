package org.poo.parsers.commands;

import org.poo.bankPair.Bank;
import org.poo.exceptions.PaymentInfoNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;

public class RejectSplitPayment implements Command {

    private String email;
    private String splitPaymentType;
    private int timestamp;

    public RejectSplitPayment(CommandInput commandInput) {
        email = commandInput.getEmail();
        splitPaymentType = commandInput.getSplitPaymentType();
        timestamp = commandInput.getTimestamp();
    }

    public void execute() {
        try {
            SplitPaymentInfo splitPaymentInfo = Bank.getInstance().getSplitPaymentByTypeAndEmail(email, splitPaymentType);
            Bank.getInstance().getSplitPayments().remove(splitPaymentInfo);

            for (String iban : splitPaymentInfo.getAccountsForSplit()) {
                // TODO add here the messages in the user's Transactions or others
            }
        } catch (PaymentInfoNotFoundException ignored) {

        }
    }
}

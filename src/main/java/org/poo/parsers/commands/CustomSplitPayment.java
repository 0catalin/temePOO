package org.poo.parsers.commands;

import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfoNonEqual;

/**
 * class implementing the method of a custom split of payment
 */
public final class CustomSplitPayment implements Command {

    private CommandInput input;


    public CustomSplitPayment(final CommandInput commandInput) {
        input = commandInput;
    }

    /**
     * adds the payment to the payment queue from the Bank
     */
    public void execute() {
        Bank.getInstance().getSplitPayments().add(new SplitPaymentInfoNonEqual(input));
    }
}

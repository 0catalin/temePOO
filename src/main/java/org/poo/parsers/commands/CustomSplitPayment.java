package org.poo.parsers.commands;

import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfoNonEqual;


public final class CustomSplitPayment implements Command {

    private CommandInput input;


    public CustomSplitPayment(final CommandInput commandInput) {
        input = commandInput;
    }

    public void execute() {
        Bank.getInstance().getSplitPayments().add(new SplitPaymentInfoNonEqual(input));
    }
}

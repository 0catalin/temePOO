package org.poo.parsers.commands;


import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;



/**
 * class implementing the split payment command
 */
public final class EqualSplitPayment implements Command {

    private final CommandInput input;


    public EqualSplitPayment(final CommandInput commandInput) {
        input = commandInput;
    }


    /**
     * adds the payment to the payment queue from the Bank
     */
    @Override
    public void execute() {
        Bank.getInstance().getSplitPayments().add(new SplitPaymentInfo(input));
    }

}

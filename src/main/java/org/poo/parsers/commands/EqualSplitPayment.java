package org.poo.parsers.commands;


import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;



/**
 * class implementing the split payment command
 */
public final class EqualSplitPayment implements Command {

    private CommandInput input;


    public EqualSplitPayment(final CommandInput commandInput) {
        input = commandInput;
    }


    /**
     * all the accounts, ibans and users are added into lists. the method
     * checks if the ibans are correct at first, if everything is okay
     * the accounts have transactions added and balances decreased
     */
    @Override
    public void execute() {
        Bank.getInstance().getSplitPayments().add(new SplitPaymentInfo(input));
    }

}

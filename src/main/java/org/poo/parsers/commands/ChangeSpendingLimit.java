package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.ChangeSpendingLimitVisitor;


/**
 * class implementing the method of changing the deposit limit
 */
public final class ChangeSpendingLimit implements Command {

    private final int timestamp;
    private final double amount;
    private final String iban;
    private final String email;


    public ChangeSpendingLimit(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        email = commandInput.getEmail();
    }


    /**
     * method that initializes the visitor and accepts it via the account
     */
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        ChangeSpendingLimitVisitor visitor
                = new ChangeSpendingLimitVisitor(amount, email, timestamp);
        account.accept(visitor);
    }
}

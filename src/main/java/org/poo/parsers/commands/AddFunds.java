package org.poo.parsers.commands;

import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.AddFundsVisitor;


/**
 * class implementing the add funds command
 */
public final class AddFunds implements Command {

    private final String email;
    private final String iban;
    private final double amount;
    private final int timestamp;


    public AddFunds(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        email = commandInput.getEmail();
        timestamp = commandInput.getTimestamp();
    }


    /**
     * creates visitor for adding funds and it gets accepted by the account
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            AddFundsVisitor visitor = new AddFundsVisitor(amount, email, timestamp);
            account.accept(visitor);
        } catch (AccountNotFoundException ignored) {

        }
    }
}

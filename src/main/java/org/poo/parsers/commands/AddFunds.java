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


    public AddFunds(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        email = commandInput.getEmail();
    }


    /**
     * adds funds to the account, if the account is not found it doesn't do anything
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            AddFundsVisitor visitor = new AddFundsVisitor(amount, email);
            account.accept(visitor);
        } catch (AccountNotFoundException ignored) {

        }
    }
}

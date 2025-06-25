package org.poo.parsers.commands;

import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;



/**
 * class implementing the add funds command
 */
public final class AddFunds implements Command {

    private final String iban;
    private final double amount;

    public AddFunds(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
    }


    /**
     * adds funds to the account, if the account is not found it doesn't do anything
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            account.setBalance(account.getBalance() + amount);
        } catch (AccountNotFoundException ignored) {

        }
    }
}

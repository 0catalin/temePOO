package org.poo.parsers.commands;

import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;



/**
 * class implementing the add funds command
 */
public final class AddFunds implements Command {
    private String iban;
    private double amount;

    public AddFunds(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
    }

    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            account.setBalance(account.getBalance() + amount);
        } catch (AccountNotFoundException e) {

        }
    }
}

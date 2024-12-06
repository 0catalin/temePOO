package org.poo.commands;

import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;

public final class AddFunds implements Command {
    private String iban;
    private double amount;

    public AddFunds(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
    }

    @Override
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        if (account == null) {
            // never happens
        } else {
            account.setBalance(account.getBalance() + amount);
        }
    }
}

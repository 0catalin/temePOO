package org.poo.commands;


import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

public final class SetMinimumBalance implements Command {
    private String iban;
    private double minBalance;

    public SetMinimumBalance(final CommandInput commandInput) {
        iban = commandInput.getAccount();
        minBalance = commandInput.getAmount();
    }

    @Override
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        if (account != null) {
            account.setMinBalance(minBalance);
        } else {
            // don't have to handle exception
        }
    }
}

package org.poo.parsers.commands;


import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the set minimum balance command
 */
public final class SetMinimumBalance implements Command {
    private String iban;
    private double minBalance;


    public SetMinimumBalance(final CommandInput commandInput) {
        iban = commandInput.getAccount();
        minBalance = commandInput.getAmount();
    }


    /**
     * if the account is valid the min balance is set
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            account.setMinBalance(minBalance);
        } catch (AccountNotFoundException e) {

        }
    }
}

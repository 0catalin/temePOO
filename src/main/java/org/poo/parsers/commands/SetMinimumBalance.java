package org.poo.parsers.commands;


import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.SetMinBalanceVisitor;

/**
 * class implementing the set minimum balance command
 */
public final class SetMinimumBalance implements Command {

    private final String iban;
    private final double minBalance;
    private int timestamp;


    public SetMinimumBalance(final CommandInput commandInput) {
        iban = commandInput.getAccount();
        minBalance = commandInput.getAmount();
        timestamp = commandInput.getTimestamp();
    }


    /**
     * if the account is valid the a visitor
     * is created and accepted to set the min balance
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);

            SetMinBalanceVisitor visitor = new SetMinBalanceVisitor(minBalance, timestamp);
            account.accept(visitor);
        } catch (AccountNotFoundException ignored) {

        }
    }
}

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
     * adds funds to the account, if the account is not found it doesn't do anything
     */
    @Override
    public void execute() {
        try {
            if (email.equals("Christophe-Adrien_Coulon@outlook.fr")) {
                int i = 1;
            }
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            AddFundsVisitor visitor = new AddFundsVisitor(amount, email, timestamp);
            account.accept(visitor);
        } catch (AccountNotFoundException ignored) {

        }
    }
}

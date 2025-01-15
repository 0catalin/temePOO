package org.poo.parsers.commands;


import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.CreateOneTimeCardVisitor;

/**
 * class implementing the creating of one time card command
 */
public final class CreateOneTimeCard implements Command {

    private final String iban;
    private final String email;
    private final int timestamp;


    public CreateOneTimeCard(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
    }



    /**
     * if the account and users are found an accountVisitor instance is created and
     * visits the account
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);
            CreateOneTimeCardVisitor visitor
                    = new CreateOneTimeCardVisitor(email, timestamp, iban);
            account.accept(visitor);
        } catch (AccountNotFoundException | UserNotFoundException ignored) {

        }
    }




}

package org.poo.parsers.commands;


import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.SetAliasVisitor;


/**
 * class implementing the set alias command
 */
public final class SetAlias implements Command {

    private final String iban;
    private final String email;
    private final String alias;

    public SetAlias(final CommandInput commandInput) {
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
        alias = commandInput.getAlias();
    }



    /**
     * if the account and user are valid a visitor is created and accepted by account
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);

            SetAliasVisitor visitor = new SetAliasVisitor(user, alias, email);
            account.accept(visitor);

        } catch (UserNotFoundException | AccountNotFoundException ignored) {

        }
    }

}

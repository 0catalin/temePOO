package org.poo.parsers.commands;


import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;


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
     * if the account and user are valid the alias is set
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);
            if (user.getAccounts().contains(account)) {
                account.setAlias(alias);
            }
        } catch (UserNotFoundException | AccountNotFoundException ignored) {

        }
    }

}

package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.AddNewBusinessAssociateVisitor;

/**
 * class implementing addBusinessAccount method
 */
public final class AddNewBusinessAssociate implements Command {

    private final String email;
    private final String role;
    private final String iban;



    public AddNewBusinessAssociate(final CommandInput commandInput) {
        email = commandInput.getEmail();
        role = commandInput.getRole();
        iban = commandInput.getAccount();
    }


    /**
     * method that initializes the visitor and accepts it via the account
     */
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        AddNewBusinessAssociateVisitor visitor = new AddNewBusinessAssociateVisitor(email, role);
        account.accept(visitor);

    }
}

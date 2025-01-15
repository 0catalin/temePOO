package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.AddNewBusinessAssociateVisitor;

public class AddNewBusinessAssociate implements Command{
    private final int timestamp;
    private final String email;
    private final String role;
    private final String iban;

    public AddNewBusinessAssociate(CommandInput commandInput) {
        email = commandInput.getEmail();
        role = commandInput.getRole();
        timestamp = commandInput.getTimestamp();
        iban = commandInput.getAccount();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        AddNewBusinessAssociateVisitor visitor = new AddNewBusinessAssociateVisitor(email, role);
        account.accept(visitor);

    }
}

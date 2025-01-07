package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;

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
        ((BusinessAccount )account).addNewBusinessAssociate(email, role);

    }
}

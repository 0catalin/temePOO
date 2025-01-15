package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.ChangeDepositLimitVisitor;


/**
 * class implementing the method of changing the deposit limit
 */
public final class ChangeDepositLimit implements Command {

    private int timestamp;
    private double amount;
    private String iban;
    private String email;



    public ChangeDepositLimit(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        email = commandInput.getEmail();
    }



    /**
     * method that initializes the visitor and accepts it via the account
     */
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        ChangeDepositLimitVisitor visitor = new ChangeDepositLimitVisitor(timestamp, amount, email);
        account.accept(visitor);
    }

}

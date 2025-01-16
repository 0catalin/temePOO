package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.accountVisitors.reportVisitors.BusinessReportTransactionVisitor;
import org.poo.visitors.accountVisitors.Visitor;


/**
 * class implementing the transaction business report method
 */
public final class BusinessReportTransaction implements Command {

    private final String iban;
    private final int startTimestamp;
    private final int endTimestamp;
    private final int timestamp;



    public BusinessReportTransaction(final CommandInput commandInput) {
         iban = commandInput.getAccount();
         startTimestamp = commandInput.getStartTimestamp();
         endTimestamp = commandInput.getEndTimestamp();
         timestamp = commandInput.getTimestamp();
    }


    @Override
    /**
     * method that initializes the visitor and accepts it via the account
     */
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            Visitor visitor = new BusinessReportTransactionVisitor(iban, timestamp,
                    startTimestamp, endTimestamp);
            account.accept(visitor);
        } catch (AccountNotFoundException e) {

        }
    }

}

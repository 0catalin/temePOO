package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.reportVisitors.BusinessReportTransactionVisitor;
import org.poo.visitors.reportVisitors.SpendingsReportVisitor;
import org.poo.visitors.reportVisitors.Visitor;

public class BusinessReportTransaction implements Command {

    private String iban;
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;

    public BusinessReportTransaction(CommandInput commandInput) {
         iban = commandInput.getAccount();
         startTimestamp = commandInput.getTimestamp();
         endTimestamp = commandInput.getTimestamp();
         timestamp = commandInput.getTimestamp();
    }

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

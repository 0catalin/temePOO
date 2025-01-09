package org.poo.parsers.commands;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.reportVisitors.BusinessReportCommerciantVisitor;
import org.poo.visitors.reportVisitors.BusinessReportTransactionVisitor;
import org.poo.visitors.reportVisitors.Visitor;

public class BusinessReportCommerciant implements Command {
    private String iban;
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;

    public BusinessReportCommerciant(CommandInput commandInput) {
        iban = commandInput.getAccount();
        startTimestamp = commandInput.getStartTimestamp();
        endTimestamp = commandInput.getEndTimestamp();
        timestamp = commandInput.getTimestamp();

    }


    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            Visitor visitor = new BusinessReportCommerciantVisitor(startTimestamp, endTimestamp, timestamp, iban);
            account.accept(visitor);
        } catch (AccountNotFoundException e) {

        }
    }

}

package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.ChangeDepositLimitVisitor;

public class ChangeDepositLimit implements Command {
    private int timestamp;
    private double amount;
    private String iban;
    private String email;

    public ChangeDepositLimit(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        email = commandInput.getEmail();
    }

    public void execute() {
        BusinessAccount account = (BusinessAccount) Bank.getInstance().getAccountByIBAN(iban);
        ChangeDepositLimitVisitor visitor = new ChangeDepositLimitVisitor(timestamp, amount, email);
        account.accept(visitor);
    }

}

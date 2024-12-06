package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;

public class AddFunds implements Command {
    private String IBAN;
    private double amount;

    public AddFunds(CommandInput commandInput) {
        amount = commandInput.getAmount();
        IBAN = commandInput.getAccount();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        if(account == null) {
            // never happens
        } else {
            account.setBalance(account.getBalance() + amount);
        }
    }
}
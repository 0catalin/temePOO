package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;

public class AddFunds implements Command {
    private String IBAN;
    private double amount;
    private int timestamp;

    public AddFunds(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        IBAN = commandInput.getAccount();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        if(account == null) {
            System.out.println("Account not found");
        } else {
            account.setBalance(account.getBalance() + amount);
        }
    }
}
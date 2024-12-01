package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.SavingsAccount;
import org.poo.fileio.CommandInput;

public class AddSavingsAccount implements Command{
    private String email;
    private String currency;
    private int timestamp;
    private double interestRate;
    public AddSavingsAccount(CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
        interestRate = commandInput.getInterestRate();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        SavingsAccount savingsAccount = new SavingsAccount(currency, interestRate);
        if(bank.getUserByEmail(email) != null) {
            bank.getUserByEmail(email).getAccounts().add(savingsAccount);
        } else {
            System.out.println("User Not found"); // de modificat daca nu e vreo problema
        }
    }
}

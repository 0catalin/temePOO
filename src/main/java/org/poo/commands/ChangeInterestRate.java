package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

public class ChangeInterestRate implements Command{
    private String IBAN;
    private int timestamp;
    private double interestRate;

    public ChangeInterestRate(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        IBAN = commandInput.getAccount();
        interestRate = commandInput.getInterestRate();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        if(account == null) {
            System.out.println("Account not found");
        }
         else if(account.getType().equals("savings")) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.setInterestRate(interestRate);
        } else {
            System.out.println("ACCOUNT IS NOT SAVINGS ACCOUNT");
        }

    }
}

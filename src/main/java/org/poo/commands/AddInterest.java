package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

public class AddInterest implements Command{
    private String IBAN;
    private int timestamp;

    public AddInterest(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        IBAN = commandInput.getAccount();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        if(account == null) {
            System.out.println("Account not found");
        }
        if(account.getType().equals("savings")) {
            account.setBalance(account.getBalance() * ( 1 + ((SavingsAccount)account).getInterestRate()));
        } else {
            System.out.println("ACCOUNT IS NOT SAVINGS ACCOUNT");
        }
    }
}

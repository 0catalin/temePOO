package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class ChangeInterestRate implements Command{
    private String IBAN;
    private int timestamp;
    private double interestRate;

    public ChangeInterestRate(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        IBAN = commandInput.getAccount();
        interestRate = commandInput.getInterestRate();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        if (account == null) {
            // case never happening
        }
         else if(account.getType().equals("savings")) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.setInterestRate(interestRate);
            Bank.getInstance().getUserByIBAN(IBAN).getTranzactions().add(successSet());
            Bank.getInstance().getAccountByIBAN(IBAN).getReportsSavings().add(successSet());
        } else {
            Bank.getInstance().getOutput().add(savingsAccountError());
        }

    }

    private ObjectNode savingsAccountError() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "This is not a savings account");
        outputNode.put("timestamp", timestamp);
        node.put("command", "changeInterestRate");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }

    private ObjectNode successSet() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("description", "Interest rate of the account changed to " + interestRate);
        node.put("timestamp", timestamp);
        return node;
    }
}

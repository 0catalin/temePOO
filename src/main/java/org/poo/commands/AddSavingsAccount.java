package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.SavingsAccount;
import org.poo.baseinput.User;
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
        if(bank.getUserByEmail(email) != null) {
            SavingsAccount savingsAccount = new SavingsAccount(currency, interestRate);
            User user = bank.getUserByEmail(email);
            user.getAccounts().add(savingsAccount);
            user.getTranzactions().add(addToUsersTranzactions(mapper));
        } else {
            System.out.println("User Not found"); // de modificat daca nu e vreo problema
        }
    }

    private ObjectNode addToUsersTranzactions(ObjectMapper mapper) {
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New account created");
        return output;
    }
}




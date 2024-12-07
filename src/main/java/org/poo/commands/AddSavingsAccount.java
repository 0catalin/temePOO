package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.SavingsAccount;
import org.poo.baseinput.User;
import org.poo.exceptions.UserNotFoundException;
import org.poo.fileio.CommandInput;


public final class AddSavingsAccount implements Command {
    private String email;
    private String currency;
    private int timestamp;
    private double interestRate;
    public AddSavingsAccount(final CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
        interestRate = commandInput.getInterestRate();
    }

    @Override
    public void execute() {
        try  {
            SavingsAccount savingsAccount = new SavingsAccount(currency, interestRate);
            User user = Bank.getInstance().getUserByEmail(email);
            user.getAccounts().add(savingsAccount);
            user.getTranzactions().add(addToUsersTranzactions());
            Bank.getInstance().getAccountByIBAN(savingsAccount.getIban())
                    .getReportsClassic().add(addToUsersTranzactions());
        } catch (UserNotFoundException e) {

        }
    }

    private ObjectNode addToUsersTranzactions() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New account created");
        return output;
    }
}




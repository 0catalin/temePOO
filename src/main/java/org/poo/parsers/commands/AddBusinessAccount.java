package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;

public class AddBusinessAccount implements Command {


    private String email;
    private String currency;
    private int timestamp;

    public AddBusinessAccount(CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
    }

    public void execute() {
        try {
            BusinessAccount businessAccount = new BusinessAccount(email, currency);
            User user = Bank.getInstance().getUserByEmail(email);
            user.getAccounts().add(businessAccount);
            user.getTranzactions().add(addToUsersTranzactions());
            Bank.getInstance().getAccountByIBAN(businessAccount.getIban())
                    .getReportsClassic().add(addToUsersTranzactions());
        } catch (UserNotFoundException ignored) {

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

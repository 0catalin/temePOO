package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.ClassicAccount;
import org.poo.baseinput.User;
import org.poo.exceptions.UserNotFoundException;
import org.poo.fileio.CommandInput;


public final class AddClassicAccount implements Command {
    private String email;
    private String currency;
    private int timestamp;
    public AddClassicAccount(final CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
    }

    @Override
    public void execute() {
        ClassicAccount classicAccount = new ClassicAccount(currency);
        try {
            User user = Bank.getInstance().getUserByEmail(email);
            user.getAccounts().add(classicAccount);
            user.getTranzactions().add(addToUsersTranzactions());
            Bank.getInstance().getAccountByIBAN(classicAccount.getIban())
                    .getReportsClassic().add(addToUsersTranzactions());
        } catch (UserNotFoundException e) {
            // never happens
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

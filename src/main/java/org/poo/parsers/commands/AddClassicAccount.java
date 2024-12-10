package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.ClassicAccount;
import org.poo.baseinput.User;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the add classic account command
 */
public final class AddClassicAccount implements Command {
    private String email;
    private String currency;
    private int timestamp;
    public AddClassicAccount(final CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
    }



    /**
     * creates a classic account if the user email is valid and
     * adds it to the account list and transactions
     */
    @Override
    public void execute() {
        try {
            ClassicAccount classicAccount = new ClassicAccount(currency);
            User user = Bank.getInstance().getUserByEmail(email);
            user.getAccounts().add(classicAccount);
            user.getTranzactions().add(addToUsersTranzactions());
            Bank.getInstance().getAccountByIBAN(classicAccount.getIban())
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

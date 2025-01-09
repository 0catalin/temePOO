package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.SavingsAccount;
import org.poo.baseinput.User;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the add savings account command
 */
public final class AddSavingsAccount implements Command {

    private final String email;
    private final String currency;
    private final int timestamp;
    private final double interestRate;

    public AddSavingsAccount(final CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
        interestRate = commandInput.getInterestRate();
    }


    /**
     * creates a savings account if the user email is
     * valid and adds it to the account list and transactions
     */
    @Override
    public void execute() {

        try {
            SavingsAccount savingsAccount = new SavingsAccount(currency, interestRate);
            User user = Bank.getInstance().getUserByEmail(email);
            user.getAccounts().add(savingsAccount);
            user.getTranzactions().add(addToUsersTranzactions());
            savingsAccount.getReportsSavings().add(addToUsersTranzactions());
            Bank.getInstance().getAccountByIBAN(savingsAccount.getIban())
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




package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.DeleteAccountVisitor;

/**
 * class implementing the delete account command
 */
public final class DeleteAccount implements Command {

    private final String iban;
    private final String email;
    private final int timestamp;


    public DeleteAccount(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
    }


    /**
     * method that instantiates a visitor which gets accepted by the account
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);

            DeleteAccountVisitor visitor = new DeleteAccountVisitor(user, timestamp);
            account.accept(visitor);

        } catch (AccountNotFoundException | UserNotFoundException e) {
            deleteFailure();
        }
    }






    private void deleteFailure() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("error",
                "Account couldn't be deleted - see org.poo.transactions for details");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(finalNode);
    }





}

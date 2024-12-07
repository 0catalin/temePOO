package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the delete account command
 */
public final class DeleteAccount implements Command {
    private String iban;
    private String email;
    private int timestamp;

    public DeleteAccount(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
    }

    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);
            if (!account.isEmpty()) {
                deleteFailure();
                Bank.getInstance().getUserByIBAN(account.getIban())
                        .getTranzactions().add(deleteFundsRemaining());
                account.getReportsClassic().add(deleteFundsRemaining());
            } else if (user.getAccounts().contains(account)) {
                user.getAccounts().remove(account);
                deleteSuccess();
            } else {
                deleteFailure();
            }
        } catch (AccountNotFoundException | UserNotFoundException e) {
            deleteFailure();
        }
    }

    private void deleteSuccess() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(finalNode);
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

    private ObjectNode deleteFundsRemaining() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("description", "Account couldn't be deleted - there are funds remaining");
        finalNode.put("timestamp", timestamp);
        return finalNode;
    }

}

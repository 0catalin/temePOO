package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class DeleteAccount implements Command{
    private String IBAN;
    private String email;
    private int timestamp;

    public DeleteAccount(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        IBAN = commandInput.getAccount();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        User user = Bank.getInstance().getUserByEmail(email);
        if (account == null) {
            deleteFailure();
        } else if (user == null) {
            deleteFailure();
        } else if (!account.isEmpty()) {
            deleteFailure();
            Bank.getInstance().getUserByIBAN(account.getIBAN()).getTranzactions().add(deleteFundsRemaining());
            account.getReportsClassic().add(deleteFundsRemaining());
        } else if (user.getAccounts().contains(account)) {
                user.getAccounts().remove(account);
                deleteSuccess();
        } else {
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
        outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
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
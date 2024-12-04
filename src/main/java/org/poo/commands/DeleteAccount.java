package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

public class DeleteAccount implements Command{
    private String IBAN;
    private String email;
    private int timestamp;

    public DeleteAccount(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        IBAN = commandInput.getAccount();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        User user = bank.getUserByEmail(email);
        if (account == null) {
            System.out.println("Account not found");
            deleteFailure(output, mapper);
        } else if (user == null) {
            System.out.println("User not found");
            deleteFailure(output, mapper);
        } else if (account.getBalance() != 0) {
            System.out.println("Account balance not zero");
            deleteFailure(output, mapper);
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(deleteFundsRemaining(mapper));
        } else {
            if (user.getAccounts().contains(account)) {
                user.getAccounts().remove(account);
                deleteSuccess(output, mapper);
            } else {
                System.out.println("Account does not belong to the user");
                deleteFailure(output, mapper);
            }
        }
    }

    private void deleteSuccess(ArrayNode output, ObjectMapper mapper) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        output.add(finalNode);
    }

    private void deleteFailure(ArrayNode output, ObjectMapper mapper) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        output.add(finalNode);
    }

    private ObjectNode deleteFundsRemaining(ObjectMapper mapper) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("description", "Account couldn't be deleted - there are funds remaining");
        finalNode.put("timestamp", timestamp);
        return finalNode;
    }

}
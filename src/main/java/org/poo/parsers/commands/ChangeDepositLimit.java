package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.bankPair.Bank;
import org.poo.parsers.fileio.CommandInput;

public class ChangeDepositLimit implements Command {
    private int timestamp;
    private double amount;
    private String iban;
    private String email;

    public ChangeDepositLimit(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        email = commandInput.getEmail();
    }

    public void execute() {
        BusinessAccount account = (BusinessAccount) Bank.getInstance().getAccountByIBAN(iban);

        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getRbac().hasPermissions(email, "changeDepositLimit")) {
            Bank.getInstance().getOutput().add(lackOfPermissions());
        } else if (amount < 0) {

        } else {
            account.setDepositLimit(amount);
        }
    }

    private ObjectNode lackOfPermissions() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "changeDepositLimit");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "You must be owner in order to change deposit limit.");
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }
}

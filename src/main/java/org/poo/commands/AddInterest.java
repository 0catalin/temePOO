package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import org.poo.fileio.CommandInput;

public final class AddInterest implements Command {
    private String iban;
    private int timestamp;

    public AddInterest(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        iban = commandInput.getAccount();
    }

    @Override
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
        if (account == null) {
            // never happens
        }
        if (account.getType().equals("savings")) {
            account.setBalance(account.getBalance()
                    * (1 + ((SavingsAccount) account).getInterestRate()));
        } else {
            Bank.getInstance().getOutput().add(savingsAccountError());
        }
    }

    private ObjectNode savingsAccountError() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "This is not a savings account");
        outputNode.put("timestamp", timestamp);
        node.put("command", "addInterest");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }
}

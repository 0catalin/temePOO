package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the add interest command
 */
public final class AddInterest implements Command {
    private String iban;
    private int timestamp;

    public AddInterest(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        iban = commandInput.getAccount();
    }



    /**
     * collects the interest money if the account's type is savings if the account is found
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            if (account.getType().equals("savings")) {
                account.setBalance(account.getBalance()
                        * (1 + ((SavingsAccount) account).getInterestRate()));
            } else {
                Bank.getInstance().getOutput().add(savingsAccountError());
            }
        } catch (AccountNotFoundException e) {

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

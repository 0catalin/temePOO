package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the change interest rate command
 */
public final class ChangeInterestRate implements Command {


    private final String iban;
    private final int timestamp;
    private final double interestRate;



    public ChangeInterestRate(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        iban = commandInput.getAccount();
        interestRate = commandInput.getInterestRate();
    }



    /**
     * changes the interest if the account's type is savings and if it is found
     */
    @Override
    public void execute() {
         try {
             Account account = Bank.getInstance().getAccountByIBAN(iban);
             if (account.getType().equals("savings")) {
                 SavingsAccount savingsAccount = (SavingsAccount) account;
                 savingsAccount.setInterestRate(interestRate);
                 Bank.getInstance().getUserByIBAN(iban).getTranzactions().add(successSet());
                 Bank.getInstance().getAccountByIBAN(iban).getReportsSavings().add(successSet());
             } else {
                 Bank.getInstance().getOutput().add(savingsAccountError());
             }
         } catch (AccountNotFoundException ignored) {

         }

    }



    private ObjectNode savingsAccountError() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "This is not a savings account");
        outputNode.put("timestamp", timestamp);
        node.put("command", "changeInterestRate");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }



    private ObjectNode successSet() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("description", "Interest rate of the account changed to " + interestRate);
        node.put("timestamp", timestamp);
        return node;
    }
}

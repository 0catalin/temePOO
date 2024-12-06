package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class SendMoney implements Command{
    private double amount;
    private int timestamp;
    private String IBAN;
    private String description;
    private String receiver;

    public SendMoney(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        IBAN = commandInput.getAccount();
        description = commandInput.getDescription();
        receiver = commandInput.getReceiver();
    }

    public void execute() {
        Account accountSender = Bank.getInstance().getAccountByIBAN(IBAN);
        Account accountReceiver = Bank.getInstance().getAccountByIBANOrAlias(receiver);
        User userSender = Bank.getInstance().getUserByIBAN(IBAN);
        if(accountSender == null || accountReceiver == null) {
            // have to do something with exception
        } else if (accountSender.getBalance() < amount) {
            Bank.getInstance().getUserByIBAN(accountSender.getIBAN()).getTranzactions().add(insufficientFunds());
            accountSender.getReportsClassic().add(insufficientFunds());
        } else {
            userSender.getTranzactions().add(addToSendersTranzactions(accountSender, accountReceiver));
            accountSender.getReportsClassic().add(addToSendersTranzactions(accountSender, accountReceiver));

            User userReceiver = Bank.getInstance().getUserByAccount(accountReceiver);

            userReceiver.getTranzactions().add(addToReceiversTranzactions(accountSender, accountReceiver));
            accountReceiver.getReportsClassic().add(addToReceiversTranzactions(accountSender, accountReceiver));

            accountSender.setBalance(accountSender.getBalance() - amount);
            accountReceiver.setBalance(accountReceiver.getBalance() +
                    amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(),
                            accountReceiver.getCurrency()));

        }
    }

    private ObjectNode addToSendersTranzactions(Account accountSender, Account accountReceiver) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", IBAN);
        output.put("receiverIBAN", accountReceiver.getIBAN());
        output.put("amount", "" + amount + " " + accountSender.getCurrency());
        output.put("transferType", "sent");
        return output;
    }

    private ObjectNode addToReceiversTranzactions(Account accountSender, Account accountReceiver) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", IBAN);
        output.put("receiverIBAN", accountReceiver.getIBAN());
        output.put("amount", "" + amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(),
                accountReceiver.getCurrency())+ " " + accountReceiver.getCurrency());
        output.put("transferType", "received");
        return output;
    }

    private ObjectNode insufficientFunds() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Insufficient funds");
        return finalNode;
    }

}

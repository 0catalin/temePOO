package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

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

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account accountSender = bank.getAccountByIBAN(IBAN);
        Account accountReceiver = bank.getAccountByIBANOrAlias(receiver);
        User userSender = bank.getUserByIBAN(IBAN);
        if(accountSender == null || accountReceiver == null) {
            System.out.println("Account not found");
        } else if (accountSender.getBalance() < amount) {
            bank.getUserByIBAN(accountSender.getIBAN()).getTranzactions().add(insufficientFunds(mapper));
        } else {
            userSender.getTranzactions().add(addToSendersTranzactions(mapper, accountSender, accountReceiver));
            User userReceiver = bank.getUserByAccount(accountReceiver);

            userReceiver.getTranzactions().add(addToReceiversTranzactions(bank, mapper, accountSender, accountReceiver));

            accountSender.setBalance(accountSender.getBalance() - amount);
            accountReceiver.setBalance(accountReceiver.getBalance() +
                    amount * bank.findExchangeRate(accountSender.getCurrency(),
                            accountReceiver.getCurrency()));

        }
    }

    private ObjectNode addToSendersTranzactions(ObjectMapper mapper, Account accountSender, Account accountReceiver) {
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", IBAN);
        output.put("receiverIBAN", accountReceiver.getIBAN());
        output.put("amount", "" + amount + " " + accountSender.getCurrency());
        output.put("transferType", "sent");
        return output;
    }

    private ObjectNode addToReceiversTranzactions(Bank bank, ObjectMapper mapper, Account accountSender, Account accountReceiver) {
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", IBAN);
        output.put("receiverIBAN", accountReceiver.getIBAN());
        output.put("amount", "" + amount * bank.findExchangeRate(accountSender.getCurrency(),
                accountReceiver.getCurrency())+ " " + accountReceiver.getCurrency());
        output.put("transferType", "received");
        return output;
    }

    private ObjectNode insufficientFunds(ObjectMapper mapper) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Insufficient funds");
        return finalNode;
    }

}

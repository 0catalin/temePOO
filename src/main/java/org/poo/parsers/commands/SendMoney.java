package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.SendMoneyVisitor;


/**
 * class implementing the send money command
 */
public final class SendMoney implements Command {

    private final double amount;
    private final int timestamp;
    private final String iban;
    private final String description;
    private final String receiver;
    private final String email;


    public SendMoney(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        description = commandInput.getDescription();
        receiver = commandInput.getReceiver();
        email = commandInput.getEmail();
    }


    /**
     * tries to get the account sender, if it does not find it : it's printed error to
     * output, else a visitor is initialized and accepted by the account
     */
    @Override
    public void execute() {



        boolean executeFlag = true;
        try {
            Account accountSender = Bank.getInstance().getAccountByIBANOrAlias(iban);
        }  catch (AccountNotFoundException e) {
            Bank.getInstance().getOutput().add(userNotFound());
            executeFlag = false;
        }

        if (executeFlag) {
            Account accountSender = Bank.getInstance().getAccountByIBANOrAlias(iban);
            SendMoneyVisitor visitor = new SendMoneyVisitor(timestamp,
                    iban, description, receiver, email, amount);
            accountSender.accept(visitor);
        }
    }






    private ObjectNode addToSendersTranzactions(final Account accountSender,
                                                final Account accountReceiver) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", iban);
        output.put("receiverIBAN", accountReceiver.getIban());
        output.put("amount", amount + " " + accountSender.getCurrency());
        output.put("transferType", "sent");
        return output;
    }



    private ObjectNode addToReceiversTranzactions(final Account accountSender,
                                                  final Account accountReceiver) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", iban);
        output.put("receiverIBAN", accountReceiver.getIban());
        output.put("amount", +amount * Bank.getInstance().
                findExchangeRate(accountSender.getCurrency(),
                accountReceiver.getCurrency()) + " " + accountReceiver.getCurrency());
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

    private ObjectNode userNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "sendMoney");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "User not found");
        outputNode.put("timestamp", timestamp);
        output.set("output", outputNode);
        output.put("timestamp", timestamp);
        return output;
    }

}

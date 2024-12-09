package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;


/**
 * class implementing the send money command
 */
public final class SendMoney implements Command {
    private double amount;
    private int timestamp;
    private String iban;
    private String description;
    private String receiver;


    public SendMoney(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        iban = commandInput.getAccount();
        description = commandInput.getDescription();
        receiver = commandInput.getReceiver();
    }


    /**
     * gets the accounts and if they are valid checks whether
     * the sender has enough balance, and if he does multiple transactions
     * are added to their accounts and the balances are set afterwards
     */
    @Override
    public void execute() {
        try {
            Account accountSender = Bank.getInstance().getAccountByIBAN(iban);
            Account accountReceiver = Bank.getInstance().getAccountByIBANOrAlias(receiver);
            User userSender = Bank.getInstance().getUserByIBAN(iban);
            if (accountSender.getBalance() < amount) {

                Bank.getInstance().getUserByIBAN(accountSender.getIban())
                        .getTranzactions().add(insufficientFunds());
                accountSender.getReportsClassic().add(insufficientFunds());

            } else {

                userSender.getTranzactions()
                        .add(addToSendersTranzactions(accountSender, accountReceiver));
                accountSender.getReportsClassic()
                        .add(addToSendersTranzactions(accountSender, accountReceiver));

                User userReceiver = Bank.getInstance().getUserByAccount(accountReceiver);

                userReceiver.getTranzactions()
                        .add(addToReceiversTranzactions(accountSender, accountReceiver));
                accountReceiver.getReportsClassic()
                        .add(addToReceiversTranzactions(accountSender, accountReceiver));

                accountSender.setBalance(accountSender.getBalance() - amount);
                accountReceiver.setBalance(accountReceiver.getBalance()
                        + amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(),
                        accountReceiver.getCurrency()));

            }
        } catch (AccountNotFoundException e) {

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
        output.put("amount", "" + amount + " " + accountSender.getCurrency());
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
        output.put("amount", "" + amount * Bank.getInstance().
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

}

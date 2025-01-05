package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.CommerciantNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.strategies.Strategy;
import org.poo.strategies.StrategyFactory;


/**
 * class implementing the send money command
 */
public final class SendMoney implements Command {

    private double amount;
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
            Commerciant commerciant = Bank.getInstance().getCommerciantByIban(receiver);


            if (accountSender.getBalance() < amount * userSender.getPlanMultiplier(amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(), "RON"))) {

                Bank.getInstance().getUserByIBAN(accountSender.getIban())
                        .getTranzactions().add(insufficientFunds());
                accountSender.getReportsClassic().add(insufficientFunds());
            } else {
                //double cashback = 0;
                //cashback += accountSender.getTransactionCashback(commerciant) * amount;

                double newAmount = amount * userSender.getPlanMultiplier(amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(), "RON"));
                //Strategy strategy = StrategyFactory.createStrategy(commerciant, accountSender, newAmount);
                //strategy.execute();
                //cashback += accountSender.getSpendingCashBack(commerciant, userSender.getServicePlan()) * amount;


                userSender.getTranzactions()
                        .add(addToSendersTranzactions(accountSender, accountReceiver));
                accountSender.getReportsClassic()
                        .add(addToSendersTranzactions(accountSender, accountReceiver));
                accountSender.setBalance(accountSender.getBalance() - newAmount);

                //accountSender.setBalance(accountSender.getBalance() + cashback);

            }


        } catch (AccountNotFoundException e) {
            Bank.getInstance().getOutput().add(userNotFound());
        } catch (CommerciantNotFoundException e) {


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
                double extraAmount = amount * userSender.getPlanMultiplier(amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(), "RON"));
                accountSender.setBalance(accountSender.getBalance() - extraAmount);
                accountReceiver.setBalance(accountReceiver.getBalance()
                        + amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(),
                        accountReceiver.getCurrency()));
            }
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

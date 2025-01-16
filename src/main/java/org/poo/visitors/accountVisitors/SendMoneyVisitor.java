package org.poo.visitors.accountVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.visitors.accountVisitors.reportVisitors.spendingInfo.SpendingUserInfoBuilder;
import org.poo.accounts.Account;
import org.poo.accounts.business.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.CommerciantNotFoundException;
import org.poo.strategies.Strategy;
import org.poo.strategies.StrategyFactory;


/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class SendMoneyVisitor implements Visitor {

    private final double amount;
    private final int timestamp;
    private final String iban;
    private final String description;
    private final String receiver;
    private final String email;

    public SendMoneyVisitor(final int timestamp, final String iban,
                            final String description, final String receiver,
                            final String email, final double amount) {
        this.timestamp = timestamp;
        this.iban = iban;
        this.description = description;
        this.receiver = receiver;
        this.email = email;
        this.amount = amount;

    }

    /**
     *  runs the function for both classic and savings accounts
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {
        sendMoneyClassicOrSavings(account);
    }


    /**
     * there are some checks done about the spending limit and then the method runs
     * on 2 branches: the valid commerciant one and the valid user one
     * - in the commerciant one the cashback is received like in the PayOnline command
     * - in the user one a user simply receives the money and the other send it,
     * the transactions being added in their reports and user transactions
     * @param accountSender the business account
     */
    public void visit(final BusinessAccount accountSender) {
        if (!accountSender.getEmailToCards().containsKey(email)) {
            return;
        } else if (accountSender.getSpendingLimit(email) < amount) {
            return;
        } else {
            try {

                Account accountReceiver = Bank.getInstance().getAccountByIBANOrAlias(receiver);
                User userSender = Bank.getInstance().getUserByIBAN(accountSender.getIban());

                User ownerUser = Bank.getInstance().getUserByAccount(accountSender);
                // case when the sender does not have balance (to user payment)
                if (accountSender.getBalance() < amount
                        * ownerUser.getPlanMultiplier(amount
                        * Bank.getInstance()
                        .findExchangeRate(accountSender.getCurrency(), "RON"))) {

                    Bank.getInstance().getUserByIBAN(accountSender.getIban())
                            .getTranzactions().add(insufficientFunds());
                    accountSender.getReportsClassic().add(insufficientFunds());
                    // case when the sender has balance (to user payment)
                } else {
                    userSender.getTranzactions()
                            .add(addToSendersTranzactions(accountSender, receiver));
                    accountSender.getReportsClassic()
                            .add(addToSendersTranzactions(accountSender, receiver));

                    userSender.checkFivePayments(amount
                            * Bank.getInstance().findExchangeRate(
                                    accountSender.getCurrency(), "RON"),
                            accountSender.getIban(), timestamp);
                    accountSender.getSpendingUserInfos().add(
                            new SpendingUserInfoBuilder(email, timestamp)
                                    .spent(amount).build());

                    User userReceiver = Bank.getInstance().getUserByAccount(accountReceiver);

                    userReceiver.getTranzactions()
                            .add(addToReceiversTranzactions(accountSender, accountReceiver));
                    accountReceiver.getReportsClassic()
                            .add(addToReceiversTranzactions(accountSender, accountReceiver));
                    double extraAmount = amount * ownerUser.getPlanMultiplier(
                            amount * Bank.getInstance()
                                    .findExchangeRate(accountSender.getCurrency(), "RON"));
                    accountSender.setBalance(accountSender.getBalance() - extraAmount);
                    accountReceiver.setBalance(accountReceiver.getBalance()
                            + amount * Bank.getInstance()
                            .findExchangeRate(accountSender.getCurrency(),
                            accountReceiver.getCurrency()));
                }
            } catch (AccountNotFoundException e) {
                // if accountReceiver is not a regular account
                // we try to find by commerciant
                User userSender = Bank.getInstance().getUserByIBAN(accountSender.getIban());
                User ownerUser = Bank.getInstance().getUserByAccount(accountSender);


                try {
                    Commerciant commerciant = Bank.getInstance().getCommerciantByIban(receiver);
                    // valid commerciant but not enough amount
                    if (accountSender.getBalance() < amount
                            * ownerUser.getPlanMultiplier(amount
                            * Bank.getInstance()
                            .findExchangeRate(accountSender.getCurrency(), "RON"))) {

                        Bank.getInstance().getUserByIBAN(accountSender.getIban())
                                .getTranzactions().add(insufficientFunds());
                        accountSender.getReportsClassic().add(insufficientFunds());
                        // valid commerciant and enough amount
                    } else {
                        double cashback = 0;
                        cashback += accountSender.getTransactionCashback(commerciant) * amount;

                        double newAmount = amount * ownerUser
                                .getPlanMultiplier(amount
                                        * Bank.getInstance()
                                        .findExchangeRate(accountSender.getCurrency(), "RON"));
                        Strategy strategy = StrategyFactory
                                .createStrategy(commerciant, accountSender, newAmount);
                        strategy.execute();
                        cashback += accountSender
                                .getSpendingCashBack(
                                        commerciant, ownerUser.getServicePlan()) * amount;



                        accountSender.setBalance(accountSender.getBalance() - newAmount);
                        userSender.checkFivePayments(amount
                                * Bank.getInstance()
                                .findExchangeRate(accountSender.getCurrency(), "RON"),
                                accountSender.getIban(), timestamp);
                        accountSender.getSpendingUserInfos().add(
                                new SpendingUserInfoBuilder(email, timestamp).spent(amount)
                                        .commerciant(commerciant.getCommerciant()).build());
                        accountSender.setBalance(accountSender.getBalance() + cashback);
                    }
                    // invalid commerciant and accountReceiver
                } catch (CommerciantNotFoundException ignored) {
                    Bank.getInstance().getOutput().add(userNotFound());
                }

            }
        }
    }


    /**
     *  runs the function for both classic and savings accounts
     * @param account the savings account
     */
    public void visit(final SavingsAccount account) {
        sendMoneyClassicOrSavings(account);
    }




    private ObjectNode addToSendersTranzactions(final Account accountSender,
                                                final String receiverIban) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", iban);
        output.put("receiverIBAN", receiverIban);
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


    /**
     * the function runs on 2 branches: the valid commerciant one and the valid user one
     * - in the commerciant one the cashback is received like in the PayOnline command
     * - in the user one a user simply receives the money and the other send it,
     * the transactions being added in their reports and user transactions
     * @param accountSender the classic or savings account
     */
    private void sendMoneyClassicOrSavings(final Account accountSender) {
        try {
            Account accountReceiver = Bank.getInstance().getAccountByIBANOrAlias(receiver);
            User userSender = Bank.getInstance().getUserByIBAN(iban);
            // case when the sender does not have balance (to user payment)
            if (accountSender.getBalance() < amount * userSender
                    .getPlanMultiplier(amount
                            * Bank.getInstance()
                            .findExchangeRate(accountSender.getCurrency(), "RON"))) {

                Bank.getInstance().getUserByIBAN(accountSender.getIban())
                        .getTranzactions().add(insufficientFunds());
                accountSender.getReportsClassic().add(insufficientFunds());
                // case when the sender has balance (to user payment)
            } else {
                userSender.getTranzactions()
                        .add(addToSendersTranzactions(accountSender, receiver));
                accountSender.getReportsClassic()
                        .add(addToSendersTranzactions(accountSender, receiver));

                userSender.checkFivePayments(amount
                        * Bank.getInstance().findExchangeRate(
                                accountSender.getCurrency(), "RON"),
                        accountSender.getIban(), timestamp);

                User userReceiver = Bank.getInstance().getUserByAccount(accountReceiver);


                accountReceiver.getReportsClassic()
                        .add(addToReceiversTranzactions(accountSender, accountReceiver));
                userReceiver.getTranzactions()
                        .add(addToReceiversTranzactions(accountSender, accountReceiver));
                double extraAmount = amount * userSender.getPlanMultiplier(
                        amount * Bank.getInstance()
                                .findExchangeRate(accountSender.getCurrency(), "RON"));
                accountSender.setBalance(accountSender.getBalance() - extraAmount);
                accountReceiver.setBalance(accountReceiver.getBalance()
                        + amount * Bank.getInstance().findExchangeRate(accountSender.getCurrency(),
                        accountReceiver.getCurrency()));
            }
        } catch (AccountNotFoundException e) {
            // if accountReceiver is not a regular account
            // we try to find by commerciant
            User userSender = Bank.getInstance().getUserByIBAN(iban);

            try {
                Commerciant commerciant = Bank.getInstance().getCommerciantByIban(receiver);
                // valid commerciant but not enough amount
                if (accountSender.getBalance() < amount
                        * userSender.getPlanMultiplier(amount
                        * Bank.getInstance().findExchangeRate(
                                accountSender.getCurrency(), "RON"))) {

                    Bank.getInstance().getUserByIBAN(accountSender.getIban())
                            .getTranzactions().add(insufficientFunds());
                    accountSender.getReportsClassic().add(insufficientFunds());
                } else {
                    // valid commerciant but enough amount
                    double cashback = 0;
                    cashback += accountSender.getTransactionCashback(commerciant) * amount;

                    double newAmount = amount * userSender
                            .getPlanMultiplier(amount
                                    * Bank.getInstance()
                                    .findExchangeRate(accountSender.getCurrency(), "RON"));
                    Strategy strategy = StrategyFactory
                            .createStrategy(commerciant, accountSender, newAmount);
                    strategy.execute();
                    cashback += accountSender.getSpendingCashBack(
                            commerciant, userSender.getServicePlan()) * amount;


                    userSender.getTranzactions()
                            .add(addToSendersTranzactions(accountSender, receiver));
                    accountSender.setBalance(accountSender.getBalance() - newAmount);
                    userSender.checkFivePayments(amount
                            * Bank.getInstance().findExchangeRate(
                                    accountSender.getCurrency(), "RON"),
                            accountSender.getIban(), timestamp);
                    accountSender.setBalance(accountSender.getBalance() + cashback);
                }
                // invalid commerciant and accountReceiver
            } catch (CommerciantNotFoundException ignored) {
                Bank.getInstance().getOutput().add(userNotFound());
            }

        }
    }
}

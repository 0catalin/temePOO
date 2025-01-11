package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.SpendingUserInfo;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.cards.Card;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.parsers.commands.PayOnline;
import org.poo.strategies.Strategy;
import org.poo.strategies.StrategyFactory;
import org.poo.visitors.reportVisitors.Visitor;

public class PayOnlineAccountVisitor implements Visitor {

    private final String cardNumber;
    private double amount;
    private final int timestamp;
    private final String currency;
    private final String commerciant;
    private final String email;


    public PayOnlineAccountVisitor(String cardNumber, double amount, int timestamp, String currency, String commerciant, String email) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.timestamp = timestamp;
        this.currency = currency;
        this.commerciant = commerciant;
        this.email = email;
    }

    public void visit(ClassicAccount account) {
                Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
                User user = Bank.getInstance().getUserByEmail(email);
                if (timestamp == 248) {
                    int i = 1;
                }
                double cashback = 0;
                double paymentAmount = amount * Bank.getInstance()
                        .findExchangeRate(currency, account.getCurrency());
                amount = paymentAmount;
                if (!user.getAccounts().contains(account)) {
                    cardNotFound();
                } else if (account.getBalance() != 0) {
                    if (account.getBalance() < paymentAmount * user.getPlanMultiplier(paymentAmount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"))) {
                        user.getTranzactions().add(insufficientFunds());
                        account.getReportsClassic().add(insufficientFunds());
                    } else { // TODO MIGHT NEED TO ADD THE CASE WHERE THE BAL IS GREATER THAN MINBAL
                        cashback += account.getTransactionCashback(Bank.getInstance().getCommerciantByName(commerciant)) * paymentAmount;
                        paymentAmount *= user.getPlanMultiplier(paymentAmount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"));
                        PayOnlineVisitor visitor = new PayOnlineVisitor(paymentAmount, timestamp,
                                commerciant, account, amount); // asta pusa cu 2 randuri mai sus
                        if (card.accept(visitor)) { // si asta, ultimele 4 se intampla doar daca returneaza True acceptul
                            Strategy strategy = StrategyFactory.createStrategy(Bank.getInstance().getCommerciantByName(commerciant), account, paymentAmount);
                            strategy.execute();
                            cashback += account.getSpendingCashBack(Bank.getInstance().getCommerciantByName(commerciant), user.getServicePlan()) * amount;
                            account.setBalance(account.getBalance() + cashback);
                            user.checkFivePayments(amount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"), account.getIban(), timestamp);
                        }
                    }
                }
    }



    public void visit(BusinessAccount account) {
        Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
        User user = Bank.getInstance().getUserByEmail(email);

        double cashback = 0;
        double paymentAmount = amount * Bank.getInstance()
                .findExchangeRate(currency, account.getCurrency());
        amount = paymentAmount;
        //double initialBalance = account.getBalance();
        if (timestamp >= 531 && timestamp <= 600 && cardNumber.equals("9281102140265301")) {
            System.out.println(paymentAmount + " " + currency + " " + email);
        }


        if (!account.getEmailToCards().containsKey(email)) {
            cardNotFound();
        } else if (account.getBalance() != 0) {
            if (account.getBalance() - account.getMinBalance() < paymentAmount * user.getPlanMultiplier(paymentAmount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"))) {

            } else {
                if (account.getSpendingLimit(email) > paymentAmount * user.getPlanMultiplier(paymentAmount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"))) {
                    cashback += account.getTransactionCashback(Bank.getInstance().getCommerciantByName(commerciant)) * paymentAmount;
                    paymentAmount *= user.getPlanMultiplier(paymentAmount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"));
                    PayOnlineVisitor visitor = new PayOnlineVisitor(paymentAmount, timestamp,
                            commerciant, account, amount); // asta pusa cu 2 randuri mai sus
                    if (card.accept(visitor)) { // si asta, ultimele 4 se intampla doar daca returneaza True acceptul
                        Strategy strategy = StrategyFactory.createStrategy(Bank.getInstance().getCommerciantByName(commerciant), account, paymentAmount);
                        strategy.execute();
                        cashback += account.getSpendingCashBack(Bank.getInstance().getCommerciantByName(commerciant), user.getServicePlan()) * amount;
                        account.setBalance(account.getBalance() + cashback);
                        account.getSpendingUserInfos().add(new SpendingUserInfo(0, amount, email, timestamp, commerciant));
                        user.checkFivePayments(amount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"), account.getIban(), timestamp);
                        if (timestamp >= 531 && timestamp <= 600 && cardNumber.equals("9281102140265301")) {
                            System.out.println("Cashback :" + cashback);
                        }
                    }
                } else {
                    // this is for when it is above the spending limit
                }
            }


        }

    }



    public void visit(SavingsAccount account) {
        Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
        User user = Bank.getInstance().getUserByEmail(email);
        double cashback = 0;
        double paymentAmount = amount * Bank.getInstance()
                .findExchangeRate(currency, account.getCurrency());
        amount = paymentAmount;
        if (!user.getAccounts().contains(account)) {
            cardNotFound();
        } else if (account.getBalance() != 0) {
            if (account.getBalance() < paymentAmount * user.getPlanMultiplier(paymentAmount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"))) {
                user.getTranzactions().add(insufficientFunds());
                account.getReportsClassic().add(insufficientFunds());
            } else { // TODO MIGHT NEED TO ADD THE CASE WHERE THE BAL IS GREATER THAN MINBAL
                cashback += account.getTransactionCashback(Bank.getInstance().getCommerciantByName(commerciant)) * paymentAmount;
                paymentAmount *= user.getPlanMultiplier(paymentAmount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"));
                PayOnlineVisitor visitor = new PayOnlineVisitor(paymentAmount, timestamp,
                        commerciant, account, amount); // asta pusa cu 2 randuri mai sus
                if (card.accept(visitor)) { // si asta, ultimele 4 se intampla doar daca returneaza True acceptul
                    Strategy strategy = StrategyFactory.createStrategy(Bank.getInstance().getCommerciantByName(commerciant), account, paymentAmount);
                    strategy.execute();
                    cashback += account.getSpendingCashBack(Bank.getInstance().getCommerciantByName(commerciant), user.getServicePlan()) * amount;
                    account.setBalance(account.getBalance() + cashback);
                    user.checkFivePayments(amount * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"), account.getIban(), timestamp);
                }
            }
        }
    }

    private ObjectNode insufficientFunds() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Insufficient funds");
        return finalNode;
    }

    private void cardNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "payOnline");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(finalNode);
    }
}

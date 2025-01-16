package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.SpendingUserInfoBuilder;
import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.cards.Card;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.strategies.Strategy;
import org.poo.strategies.StrategyFactory;
import org.poo.visitors.reportVisitors.Visitor;



/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class PayOnlineAccountVisitor implements Visitor {

    private final String cardNumber;
    private double amount;
    private final int timestamp;
    private final String currency;
    private final String commerciant;
    private final String email;


    public PayOnlineAccountVisitor(final String cardNumber, final double amount,
                                   final int timestamp, final String currency,
                                   final String commerciant, final String email) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.timestamp = timestamp;
        this.currency = currency;
        this.commerciant = commerciant;
        this.email = email;
    }




    /**
     * runs the function for both classic and savings accounts
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {
        payOnlineSavingsOrClassic(account);
    }




    /**
     * if the account has the card, the balance is not 0, the
     * spending limit is lower than the amount to be paid we get the transaction
     * cashback first, then if the amount can be paid we execute the cardVisitor and
     * the strategy, get the 2nd cashback and add the cashback money to the account
     * then, we check for 5 payments to maybe upgrade to gold
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {
        Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
        User user = Bank.getInstance().getUserByEmail(email);
        User ownerUser = Bank.getInstance().getUserByAccount(account);

        double cashback = 0;
        double paymentAmount = amount * Bank.getInstance()
                .findExchangeRate(currency, account.getCurrency());
        amount = paymentAmount;



        if (!account.getEmailToCards().containsKey(email)) {
            cardNotFound();
        } else if (account.getBalance() != 0) {
            if (account.getBalance() - account.getMinBalance() < paymentAmount
                    * ownerUser.getPlanMultiplier(paymentAmount * Bank.getInstance()
                    .findExchangeRate(account.getCurrency(), "RON"))) {
                return;
            } else {
                if (account.getSpendingLimit(email) > paymentAmount
                        * ownerUser.getPlanMultiplier(paymentAmount
                        * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"))) {
                    cashback += account.getTransactionCashback(Bank.getInstance()
                            .getCommerciantByName(commerciant)) * paymentAmount;
                    paymentAmount *= ownerUser.getPlanMultiplier(paymentAmount
                            * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"));
                    PayOnlineVisitor visitor = new PayOnlineVisitor(paymentAmount, timestamp,
                            commerciant, account, amount);


                    if (card.accept(visitor)) {
                        Strategy strategy = StrategyFactory.createStrategy(Bank.getInstance()
                                .getCommerciantByName(commerciant), account, paymentAmount);
                        strategy.execute();

                        cashback += account.getSpendingCashBack(Bank.getInstance()
                                .getCommerciantByName(commerciant),
                                ownerUser.getServicePlan()) * amount;
                        account.setBalance(account.getBalance() + cashback);
                        account.getSpendingUserInfos().add(
                                new SpendingUserInfoBuilder(email, timestamp)
                                .spent(amount).commerciant(commerciant).build());

                        user.checkFivePayments(amount * Bank.getInstance()
                                .findExchangeRate(account.getCurrency(), "RON"),
                                account.getIban(), timestamp);
                    }
                } else {
                    // this is for when it is above the spending limit
                    return;
                }
            }


        }

    }



    /**
     * runs the function for both classic and savings accounts
     * @param account the classic account
     */
    public void visit(final SavingsAccount account) {
        payOnlineSavingsOrClassic(account);
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



    /**
     * if the account has the card, and the balance is not 0 we get the transaction
     * cashback first, then if the amount can be paid we execute the cradVisitor and
     * the strategy, get the 2nd cashback and add the cashback money to the account
     * then, we check for 5 payments to maybe upgrade to gold
     * @param account the classic or savings account
     */
    private void payOnlineSavingsOrClassic(final Account account) {
        Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
        User user = Bank.getInstance().getUserByEmail(email);
        double cashback = 0;
        double paymentAmount = amount * Bank.getInstance()
                .findExchangeRate(currency, account.getCurrency());
        amount = paymentAmount;

        if (!user.getAccounts().contains(account)) {
            cardNotFound();
        } else if (account.getBalance() != 0) {
            if (account.getBalance() < paymentAmount * user.getPlanMultiplier(paymentAmount
                    * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"))) {
                user.getTranzactions().add(insufficientFunds());
                account.getReportsClassic().add(insufficientFunds());
            } else {

                cashback += account.getTransactionCashback(Bank.getInstance()
                        .getCommerciantByName(commerciant)) * paymentAmount;
                paymentAmount *= user.getPlanMultiplier(paymentAmount
                        * Bank.getInstance().findExchangeRate(account.getCurrency(), "RON"));
                PayOnlineVisitor visitor = new PayOnlineVisitor(paymentAmount, timestamp,
                        commerciant, account, amount);

                if (card.accept(visitor)) {
                    Strategy strategy = StrategyFactory.createStrategy(Bank.getInstance()
                            .getCommerciantByName(commerciant), account, paymentAmount);
                    strategy.execute();

                    cashback += account.getSpendingCashBack(Bank.getInstance()
                            .getCommerciantByName(commerciant), user.getServicePlan()) * amount;
                    account.setBalance(account.getBalance() + cashback);

                    user.checkFivePayments(amount * Bank.getInstance()
                            .findExchangeRate(account.getCurrency(), "RON"),
                            account.getIban(), timestamp);
                }
            }
        }
    }
}

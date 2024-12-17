package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.visitors.PayOnlineVisitor;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;
import org.poo.parsers.fileio.CommandInput;
/**
 * class implementing the pay online command
 */
public final class PayOnline implements Command {

    private final String cardNumber;
    private final double amount;
    private final int timestamp;
    private final String currency;
    private final String commerciant;
    private final String email;



    public PayOnline(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        amount = commandInput.getAmount();
        commerciant = commandInput.getCommerciant();
        cardNumber = commandInput.getCardNumber();
    }



    /**
     * if the card and user are found and the card is in the user's accounts and the card
     * has more than 0 balance a visitor instance is created and the card accepts the visitor
     */
    @Override
    public void execute() {
        try {
            Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
            User user = Bank.getInstance().getUserByEmail(email);
            Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
            if (!user.getAccounts().contains(account)) {
                cardNotFound();
            } else if (account.getBalance() != 0) {
                double paymentAmount = amount * Bank.getInstance()
                        .findExchangeRate(currency, account.getCurrency());
                PayOnlineVisitor visitor = new PayOnlineVisitor(paymentAmount, timestamp,
                        commerciant, account);
                card.accept(visitor);
            }
        } catch (CardNotFoundException e) {
            cardNotFound();
        } catch (UserNotFoundException ignored) {

        }

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

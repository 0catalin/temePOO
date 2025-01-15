package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.cards.Card;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;


/**
 * class implementing the method of cash withdrawal
 */
public final class CashWithdrawal implements Command {

    private final double amount;
    private final String cardNumber;
    private final String email;
    private final String location;
    private final int timestamp;


    public CashWithdrawal(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        cardNumber = commandInput.getCardNumber();
        email = commandInput.getEmail();
        location = commandInput.getLocation();
        timestamp = commandInput.getTimestamp();
    }



    @Override
    /**
     * removes balance from the account if the account has enough money,
     * is valid and the card is the user's
     */
    public void execute() {
        try {
            Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
            User user = Bank.getInstance().getUserByEmail(email);
            Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
            card = user.getCardByCardNumber(cardNumber);
            if (amount * Bank.getInstance().findExchangeRate("RON", account.getCurrency())
                    * user.getPlanMultiplier(amount)
                    > account.getBalance() - account.getMinBalance()) {
                user.getTranzactions().add(insufficientFunds());
            } else {
                account.setBalance(account.getBalance() - amount
                        * Bank.getInstance().findExchangeRate("RON", account.getCurrency())
                        * user.getPlanMultiplier(amount));
                user.getTranzactions().add(successWithdrawal());


            }
        } catch (UserNotFoundException e) {
            Bank.getInstance().getOutput().add(userNotFound());
        } catch (CardNotFoundException e) {
            Bank.getInstance().getOutput().add(cardNotFound());
        }

    }



    private ObjectNode successWithdrawal() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "Cash withdrawal of " + amount);
        output.put("amount", amount);
        return output;
    }



    private ObjectNode cardNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "cashWithdrawal");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", timestamp);
        output.set("output", outputNode);
        output.put("timestamp", timestamp);
        return output;
    }



    private ObjectNode userNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "cashWithdrawal");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "User not found");
        outputNode.put("timestamp", timestamp);
        output.set("output", outputNode);
        output.put("timestamp", timestamp);
        return output;
    }



    private ObjectNode insufficientFunds() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("description", "Insufficient funds");
        output.put("timestamp", timestamp);
        return output;
    }
}

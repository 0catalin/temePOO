package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;
import org.poo.parsers.fileio.CommandInput;


/**
 * class implementing the print users command
 */

public final class PrintUsers implements Command {

    private final int timestamp;

    public PrintUsers(final CommandInput input) {
        timestamp = input.getTimestamp();
    }


    /**
     * adds the users and their info to an objectNode put into the output node
     */
    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "printUsers");
        ArrayNode outputArray = makeUsersArray();
        commandObject.set("output", outputArray);
        commandObject.put("timestamp", timestamp);

        Bank.getInstance().getOutput().add(commandObject);
    }



    private ArrayNode makeUsersArray() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode usersArray = mapper.createArrayNode();
        for (User user : Bank.getInstance().getUsers()) {
            usersArray.add(convertUserToJson(user));
        }
        return usersArray;
    }



    private ObjectNode convertUserToJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userObject = mapper.createObjectNode();
        userObject.put("firstName", user.getFirstName());
        userObject.put("lastName", user.getLastName());
        userObject.put("email", user.getEmail());
        userObject.set("accounts", makeAccountsArray(user));
        return userObject;
    }



    private ArrayNode makeAccountsArray(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode accountsArray = mapper.createArrayNode();
        for (Account account : user.getAccounts()) {
            accountsArray.add(convertAccountToJson(account));
        }
        return accountsArray;
    }



    private ObjectNode convertAccountToJson(final Account account) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountObject = mapper.createObjectNode();
        accountObject.put("IBAN", account.getIban());
        accountObject.put("balance", account.getBalance());
        accountObject.put("currency", account.getCurrency());
        accountObject.put("type", account.getType());
        accountObject.set("cards", makeCardsArray(account));
        return accountObject;
    }



    private ArrayNode makeCardsArray(final Account account) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode cardsArray = mapper.createArrayNode();
        for (Card card : account.getCards()) {
            cardsArray.add(convertCardToJson(card));
        }
        return cardsArray;
    }



    private ObjectNode convertCardToJson(final Card card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cardObject = mapper.createObjectNode();
        cardObject.put("cardNumber", card.getCardNumber());
        cardObject.put("status", card.getStatus());
        return cardObject;
    }
}


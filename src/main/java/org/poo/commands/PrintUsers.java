package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;

public class PrintUsers implements Command{
    private int timestamp;
    public PrintUsers(CommandInput input) {
        timestamp = input.getTimestamp();
    }
    @Override
    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "printUsers");
        ArrayNode outputArray = mapper.createArrayNode();
        for (User user : bank.getUsers()) {
            ObjectNode userObject = mapper.createObjectNode();
            userObject.put("firstName", user.getFirstName());
            userObject.put("lastName", user.getLastName());
            userObject.put("email", user.getEmail());
            ArrayNode accountsArray = mapper.createArrayNode();
            for (Account account : user.getAccounts()) {
                ObjectNode accountObject = mapper.createObjectNode();
                accountObject.put("IBAN", account.getIBAN());
                accountObject.put("balance", account.getBalance());
                accountObject.put("currency", account.getCurrency());
                accountObject.put("type", account.getType());
                ArrayNode cardsArray = mapper.createArrayNode();
                for (Card card : account.getCards()) {
                    ObjectNode cardObject = mapper.createObjectNode();
                    cardObject.put("cardNumber", card.getCardNumber());
                    cardObject.put("status", card.getStatus());
                    cardsArray.add(cardObject);
                }
                accountObject.set("cards", cardsArray);
                accountsArray.add(accountObject);
            }
            userObject.set("accounts", accountsArray);
            outputArray.add(userObject);
        }
        commandObject.set("output", outputArray);
        commandObject.put("timestamp", timestamp);
        output.add(commandObject);
    }
}

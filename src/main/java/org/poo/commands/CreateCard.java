package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.cards.RegularCard;
import org.poo.fileio.CommandInput;

public class CreateCard implements Command{
    private String IBAN;
    private String email;
    private int timestamp;

    public CreateCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        IBAN = commandInput.getAccount();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        User user = bank.getUserByEmail(email);
        if (account == null) {
            System.out.println("Account not found");
        } else if (user == null) {
            System.out.println("User not found");
        } else {
            if (user.getAccounts().contains(account)) {
                Card card = new RegularCard();
                account.getCards().add(card);
                user.getTranzactions().add(addToUsersTranzactions(mapper, card));
            } else {
                System.out.println("Account does not belong to the user");
            }
        }

    }

    private ObjectNode addToUsersTranzactions(ObjectMapper mapper, Card card) {
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New card created");
        output.put("card", card.getCardNumber());
        output.put("cardHolder", email);
        output.put("account", IBAN);

        return output;
    }
}

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

import java.util.ArrayList;

public class CreateCard implements Command{
    private String IBAN;
    private String email;
    private int timestamp;

    public CreateCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        IBAN = commandInput.getAccount();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        User user = Bank.getInstance().getUserByEmail(email);
        if (account == null) {
            // never happening
        } else if (user == null) {
            System.out.println("User not found -- l");
        } else {
            if (user.getAccounts().contains(account)) {
                Card card = new RegularCard();
                account.getCards().add(card);
                user.getTranzactions().add(addToUsersTranzactions(card));
                Bank.getInstance().getAccountByIBAN(IBAN).getReportsClassic().add(addToUsersTranzactions(card));
            } else {
                System.out.println("Account does not belong to the user");
            }
        }

    }

    private ObjectNode addToUsersTranzactions(Card card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New card created");
        output.put("card", card.getCardNumber());
        output.put("cardHolder", email);
        output.put("account", IBAN);

        return output;
    }

}

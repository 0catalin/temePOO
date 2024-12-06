package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.cards.OneTimeCard;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class CreateOneTimeCard implements Command{
    private String IBAN;
    private String email;
    private int timestamp;

    public CreateOneTimeCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        IBAN = commandInput.getAccount();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        User user = Bank.getInstance().getUserByEmail(email);
        if (account == null) {
            // never happens
        } else if (user == null) {
            // never happens
        } else {
            if (user.getAccounts().contains(account)) {
                Card card = new OneTimeCard();
                user.getTranzactions().add(addToUsersTranzactions(card));
                Bank.getInstance().getAccountByIBAN(IBAN).getReportsClassic().add(addToUsersTranzactions(card));
                account.getCards().add(card);
            } else {
                // never happens
            }
        }
    }

    private ObjectNode addToUsersTranzactions(Card card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("account", IBAN);
        output.put("card", card.getCardNumber());
        output.put("cardHolder", email);
        output.put("description", "New card created");
        output.put("timestamp", timestamp);
        return output;
    }
}
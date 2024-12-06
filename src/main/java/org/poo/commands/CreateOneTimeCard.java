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

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        User user = bank.getUserByEmail(email);
        if (account == null) {
            System.out.println("Account not found");
        } else if (user == null) {
            System.out.println("User not found");
        } else {
            if (user.getAccounts().contains(account)) {
                Card card = new OneTimeCard();
                //user.getTranzactions().computeIfAbsent(addToUsersTranzactions(mapper, card), k -> new ArrayList<>()).add(IBAN);
                user.getTranzactions().add(addToUsersTranzactions(mapper, card));
                bank.getAccountByIBAN(IBAN).getReportsClassic().add(addToUsersTranzactions(mapper, card));
                account.getCards().add(card);
            } else {
                System.out.println("Account does not belong to the user");
            }
        }
    }

    private ObjectNode addToUsersTranzactions(ObjectMapper mapper, Card card) {
        ObjectNode output = mapper.createObjectNode();
        output.put("account", IBAN);
        output.put("card", card.getCardNumber());
        output.put("cardHolder", email);
        output.put("description", "New card created");
        output.put("timestamp", timestamp);
        return output;
    }
}
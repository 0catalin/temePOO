package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.cards.RegularCard;
import org.poo.fileio.CommandInput;



public final class CreateCard implements Command {
    private String iban;
    private String email;
    private int timestamp;

    public CreateCard(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
    }

    @Override
    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(iban);
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
                Bank.getInstance().getAccountByIBAN(iban)
                        .getReportsClassic().add(addToUsersTranzactions(card));
            } else {
                // no need to take action
            }
        }

    }

    private ObjectNode addToUsersTranzactions(final Card card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New card created");
        output.put("card", card.getCardNumber());
        output.put("cardHolder", email);
        output.put("account", iban);

        return output;
    }

}

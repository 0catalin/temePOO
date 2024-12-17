package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.RegularCard;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;


/**
 * class implementing the create card command
 */
public final class CreateCard implements Command {

    private final String iban;
    private final String email;
    private final int timestamp;


    public CreateCard(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
    }



    /**
     * if the account and users are found a card is created and added to the account's cards
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);
            if (user.getAccounts().contains(account)) {
                Card card = new RegularCard();
                account.getCards().add(card);
                user.getTranzactions().add(addToUsersTranzactions(card));
                Bank.getInstance().getAccountByIBAN(iban)
                        .getReportsClassic().add(addToUsersTranzactions(card));
                }
        } catch (AccountNotFoundException | UserNotFoundException ignored) {

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

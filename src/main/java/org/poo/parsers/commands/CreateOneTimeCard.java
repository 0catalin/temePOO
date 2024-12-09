package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.OneTimeCard;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the creating of one time card command
 */
public final class CreateOneTimeCard implements Command {
    private String iban;
    private String email;
    private int timestamp;


    public CreateOneTimeCard(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
    }



    /**
     * if the account and users are found a one time
     * card is created and added to the account's cards
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);
            if (user.getAccounts().contains(account)) {
                Card oneTimeCard = new OneTimeCard();
                user.getTranzactions().add(addToUsersTranzactions(oneTimeCard));
                Bank.getInstance().getAccountByIBAN(iban)
                        .getReportsClassic().add(addToUsersTranzactions(oneTimeCard));
                account.getCards().add(oneTimeCard);
            }
        } catch (AccountNotFoundException | UserNotFoundException e) {

        }
    }



    private ObjectNode addToUsersTranzactions(final Card card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("account", iban);
        output.put("card", card.getCardNumber());
        output.put("cardHolder", email);
        output.put("description", "New card created");
        output.put("timestamp", timestamp);
        return output;
    }
}

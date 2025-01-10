package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.OneTimeCard;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.CreateCardVisitor;
import org.poo.visitors.CreateOneTimeCardVisitor;

/**
 * class implementing the creating of one time card command
 */
public final class CreateOneTimeCard implements Command {

    private final String iban;
    private final String email;
    private final int timestamp;


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

            CreateOneTimeCardVisitor visitor = new CreateOneTimeCardVisitor(email, timestamp, user, iban);
            account.accept(visitor);
//                Card oneTimeCard = new OneTimeCard();
//                user.getTranzactions().add(addToUsersTranzactions(oneTimeCard));
//                Bank.getInstance().getAccountByIBAN(iban)
//                        .getReportsClassic().add(addToUsersTranzactions(oneTimeCard));
//                account.getCards().add(oneTimeCard);

        } catch (AccountNotFoundException | UserNotFoundException ignored) {

        }
    }




}

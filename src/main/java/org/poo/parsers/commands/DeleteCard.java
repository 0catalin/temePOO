package org.poo.parsers.commands;

import org.poo.accounts.cards.Card;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.CardNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.DeleteCardVisitor;

/**
 * class implementing the delete card command
 */
public final class DeleteCard implements Command {

    private final String cardNumber;
    private final int timestamp;
    private final String email;


    public DeleteCard(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
        email = commandInput.getEmail();
    }


    /**
     * it searches for the account and if it is found it deletes the card
     */
    @Override
    public void execute() {
        try {
            User user = Bank.getInstance().getUserByEmail(email);
            Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
            Card card = user.getCardByCardNumber(cardNumber);
            if (account.getBalance() == 0) {
                DeleteCardVisitor visitor = new DeleteCardVisitor(cardNumber, timestamp, email);
                account.accept(visitor);
            }
        } catch (AccountNotFoundException ignored) {

        } catch (CardNotFoundException ignored) {

        }

    }


}

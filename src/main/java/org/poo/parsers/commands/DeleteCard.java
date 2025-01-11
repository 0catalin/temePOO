package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.exceptions.AccountNotFoundException;
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
     * it searches for the account and if it is found it deletes it
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
            String iban = account.getIban();
            if (account.getBalance() == 0) {
                DeleteCardVisitor visitor = new DeleteCardVisitor(cardNumber, timestamp, email);
                account.accept(visitor);
            }

//            String email = Bank.getInstance().getUserByIBAN(iban).getEmail();
//            Bank.getInstance().getUserByIBAN(iban)
//                    .getTranzactions().add(successfulDeletion(iban, email));
//            account.getReportsClassic().add(successfulDeletion(iban, email));
//            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        } catch (AccountNotFoundException ignored) {

        }

    }



    private ObjectNode successfulDeletion(final String iban, final String email) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "The card has been destroyed");
        finalNode.put("card", cardNumber);
        finalNode.put("cardHolder", email);
        finalNode.put("account", iban);
        return finalNode;
    }
}

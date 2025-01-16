package org.poo.visitors.accountVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.business.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.RegularCard;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;


/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class CreateCardVisitor implements Visitor {

    private final String email;
    private final int timestamp;
    private final String iban;


    public CreateCardVisitor(final String email, final int timestamp, final String iban) {
        this.email = email;
        this.timestamp = timestamp;
        this.iban = iban;
    }



    /**
     * runs the method used for both classic and savings accounts
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {
        createCardSavingsOrClassic(account);
    }



    /**
     * if the user is a business associate it creates the card
     * and associates it with the user's email
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {
        Card card = new RegularCard();
        if (account.getEmailToCards().containsKey(email)) {
            account.getEmailToCards().get(email).add(card);
            account.getCards().add(card);
        }

    }




    /**
     * runs the method used for both classic and savings accounts
     * @param account the savings account
     */
    public void visit(final SavingsAccount account) {
        createCardSavingsOrClassic(account);
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



    /**
     * function that creates a card and adds it to the account's cards
     * @param account the Account given
     */
    private void createCardSavingsOrClassic(final Account account) {
        User user = Bank.getInstance().getUserByEmail(email);
        if (user.getAccounts().contains(account)) {
            Card card = new RegularCard();
            account.getCards().add(card);
            user.getTranzactions().add(addToUsersTranzactions(card));
            Bank.getInstance().getAccountByIBAN(iban)
                    .getReportsClassic().add(addToUsersTranzactions(card));
        }
    }
}

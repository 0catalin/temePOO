package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.OneTimeCard;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.visitors.reportVisitors.Visitor;



/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class CreateOneTimeCardVisitor implements Visitor {

    private final String email;
    private final int timestamp;
    private final String iban;

    public CreateOneTimeCardVisitor(final String email, final int timestamp, final String iban) {
        this.email = email;
        this.timestamp = timestamp;
        this.iban = iban;
    }

    /**
     * if the account and users are found an accountVisitor instance is created and
     * visits the account
     */
    public void visit(final ClassicAccount account) {
        createOneTimeCardCardSavingsOrClassic(account);
    }


    /**
     * if the user is a business associate it creates the card
     * and associates it with the user's email
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {
        Card card = new OneTimeCard();
        if (account.getEmailToCards().containsKey(email)) {
            account.getEmailToCards().get(email).add(card);
            account.getCards().add(card);
        }
    }


    /**
     * runs the method used for both classic and savings
     * @param account the classic account
     */
    public void visit(final SavingsAccount account) {
        createOneTimeCardCardSavingsOrClassic(account);
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

    /**
     * function that creates a card and adds it to the account's cards
     * @param account the Account given
     */
    private void createOneTimeCardCardSavingsOrClassic(final Account account) {
        User user = Bank.getInstance().getUserByEmail(email);
        if (user.getAccounts().contains(account)) {
            Card oneTimeCard = new OneTimeCard();
            user.getTranzactions().add(addToUsersTranzactions(oneTimeCard));
            Bank.getInstance().getAccountByIBAN(iban)
                    .getReportsClassic().add(addToUsersTranzactions(oneTimeCard));
            account.getCards().add(oneTimeCard);
        }
    }
}

package org.poo.visitors.accountVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.business.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.visitors.cardVisitors.ChangeCardVisitor;


/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class DeleteCardVisitor implements Visitor {

    private final String cardNumber;
    private final int timestamp;
    private final String email;


    public DeleteCardVisitor(final String cardNumber, final int timestamp, final String email) {
          this.cardNumber = cardNumber;
          this.timestamp = timestamp;
          this.email = email;
    }


    /**
     * runs method designed for both classic and savings
     */
    public void visit(final ClassicAccount account) {
        deleteCardClassicOrSavings(account);
    }


    /**
     * checks if the user has permissions and if he does it deletes the card
     * else error or nothing happens
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {
            return;
        } else if (!account.getEmailToCards().get(email).contains(
                account.getCardByCardNumber(cardNumber))) {
            if (!account.getRbac().hasPermissions(email, "deleteAnyCard")) {
                ChangeCardVisitor visitor = new ChangeCardVisitor();
                if (Bank.getInstance().getCardByCardNumber(cardNumber).accept(visitor)) {
                    account.getCards().remove(account.getCardByCardNumber(cardNumber));
                    account.getEmailToCards().get(email).remove(
                            account.getCardByCardNumber(cardNumber));
                }
            } else {
                return;
            }

        } else {
            // card was created by the user
            if (!account.getRbac().hasPermissions(email, "deleteOwnCard")) {
                ChangeCardVisitor visitor = new ChangeCardVisitor();
                if (Bank.getInstance().getCardByCardNumber(cardNumber).accept(visitor)) {
                    account.getCards().remove(account.getCardByCardNumber(cardNumber));
                    account.getEmailToCards().get(email).remove(
                            account.getCardByCardNumber(cardNumber));
                }
            } else {
                return;
            }
        }
    }


    /**
     * runs method designed for both classic and savings
     */
    public void visit(final SavingsAccount account) {
        deleteCardClassicOrSavings(account);
    }


    private ObjectNode successfulDeletion(final String iban) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "The card has been destroyed");
        finalNode.put("card", cardNumber);
        finalNode.put("cardHolder", email);
        finalNode.put("account", iban);
        return finalNode;
    }


    /**
     * deletes the card and adds to transactions
     * @param account the classic or savings account
     */
    private void deleteCardClassicOrSavings(final Account account) {
        ChangeCardVisitor visitor = new ChangeCardVisitor();
        if (Bank.getInstance().getCardByCardNumber(cardNumber).accept(visitor)) {
            Bank.getInstance().getUserByIBAN(account.getIban())
                    .getTranzactions().add(successfulDeletion(account.getIban()));
            account.getReportsClassic().add(successfulDeletion(account.getIban()));
            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        }
    }
}

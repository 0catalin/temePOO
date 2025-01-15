package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.visitors.reportVisitors.Visitor;

public final class DeleteCardVisitor implements Visitor {

    private final String cardNumber;
    private final int timestamp;
    private final String email;


    public DeleteCardVisitor(final String cardNumber, final int timestamp, final String email) {
          this.cardNumber = cardNumber;
          this.timestamp = timestamp;
          this.email = email;
    }


    public void visit(final ClassicAccount account) {
        deleteAccountClassicOrSavings(account);
    }



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



    public void visit(final SavingsAccount account) {
        deleteAccountClassicOrSavings(account);
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


    private void deleteAccountClassicOrSavings(final Account account) {
        ChangeCardVisitor visitor = new ChangeCardVisitor();
        if (Bank.getInstance().getCardByCardNumber(cardNumber).accept(visitor)) {
            Bank.getInstance().getUserByIBAN(account.getIban())
                    .getTranzactions().add(successfulDeletion(account.getIban()));
            account.getReportsClassic().add(successfulDeletion(account.getIban()));
            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        }
    }
}

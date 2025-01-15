package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.cards.OneTimeCard;
import org.poo.bankPair.Bank;
import org.poo.visitors.reportVisitors.Visitor;

public class DeleteCardVisitor implements Visitor {

    private String cardNumber;
    private int timestamp;
    private String email;


    public DeleteCardVisitor(String cardNumber, int timestamp, String email) {
          this.cardNumber = cardNumber;
          this.timestamp = timestamp;
          this.email = email;
    }


    public void visit(ClassicAccount account) {
        deleteAccountClassicOrSavings(account);
    }



    public void visit(BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getEmailToCards().get(email).contains(account.getCardByCardNumber(cardNumber))) {
            // card was not created by the user
            if (!account.getRbac().hasPermissions(email, "deleteAnyCard")) {
                ChangeCardVisitor visitor = new ChangeCardVisitor();
                if (Bank.getInstance().getCardByCardNumber(cardNumber).accept(visitor)) {
                    account.getCards().remove(account.getCardByCardNumber(cardNumber));
                    account.getEmailToCards().get(email).remove(account.getCardByCardNumber(cardNumber));
                }

            } else {

            }

        } else {
            // card was created by the user
            if (!account.getRbac().hasPermissions(email, "deleteOwnCard")) {
                ChangeCardVisitor visitor = new ChangeCardVisitor();
                if (Bank.getInstance().getCardByCardNumber(cardNumber).accept(visitor)) {
                    account.getCards().remove(account.getCardByCardNumber(cardNumber));
                    account.getEmailToCards().get(email).remove(account.getCardByCardNumber(cardNumber));
                }
            } else {

            }
        }
    }



    public void visit(SavingsAccount account) {
        deleteAccountClassicOrSavings(account);
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


    private void deleteAccountClassicOrSavings(Account account) {
        ChangeCardVisitor visitor = new ChangeCardVisitor();
        if (Bank.getInstance().getCardByCardNumber(cardNumber).accept(visitor)) {
            String email = Bank.getInstance().getUserByIBAN(account.getIban()).getEmail();
            Bank.getInstance().getUserByIBAN(account.getIban())
                    .getTranzactions().add(successfulDeletion(account.getIban(), email));
            account.getReportsClassic().add(successfulDeletion(account.getIban(), email));
            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        }
    }
}

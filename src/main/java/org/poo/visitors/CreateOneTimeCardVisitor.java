package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.visitors.reportVisitors.Visitor;

public class CreateOneTimeCardVisitor implements Visitor {

    private String email;
    private int timestamp;
    private User user;
    private String iban;

    public CreateOneTimeCardVisitor(String email, int timestamp, User user, String iban) {
        this.email = email;
        this.timestamp = timestamp;
        this.user = user;
        this.iban = iban;
    }

    public void visit(ClassicAccount account) {
        User user = Bank.getInstance().getUserByEmail(email);
        if (user.getAccounts().contains(account)) {
            Card oneTimeCard = new OneTimeCard();
            user.getTranzactions().add(addToUsersTranzactions(oneTimeCard));
            Bank.getInstance().getAccountByIBAN(iban)
                    .getReportsClassic().add(addToUsersTranzactions(oneTimeCard));
            account.getCards().add(oneTimeCard);
        }
    }



    public void visit(BusinessAccount account) {
        Card card = new OneTimeCard();
        if (account.getEmailToCards().containsKey(email)) {
            account.getEmailToCards().get(email).add(card);
            account.getCards().add(card);
        } else {

        }
    }



    public void visit(SavingsAccount account) {
        User user = Bank.getInstance().getUserByEmail(email);
        if (user.getAccounts().contains(account)) {
            Card oneTimeCard = new OneTimeCard();
            user.getTranzactions().add(addToUsersTranzactions(oneTimeCard));
            Bank.getInstance().getAccountByIBAN(iban)
                    .getReportsClassic().add(addToUsersTranzactions(oneTimeCard));
            account.getCards().add(oneTimeCard);
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

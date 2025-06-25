package org.poo.bankPair;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;

import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.CommerciantNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.exceptions.EmailNotFoundException;
import org.poo.exceptions.PaymentInfoNotFoundException;
import org.poo.parsers.InputParser;
import org.poo.splitPayment.SplitPaymentInfo;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter

/**
 * Singleton class designed to have the main classes as fields for all the operations
 */
public final class Bank {

    private ArrayList<Commerciant> commerciants;
    private Map<Pair, Double> costs;
    private ArrayList<User> users;
    private ArrayNode output;
    private ArrayList<SplitPaymentInfo> splitPayments;

    private static Bank instance = null;

    static {
        instance = new Bank();
    }

    public static Bank getInstance() {
        return instance;
    }

    private Bank() { }



    /**
     * method that reapplies parameters on every test
     * @param parser the parser with the input information
     * @param outputNode the output node which gets printed at the end in output files
     */
    public void applyParams(final InputParser parser, final ArrayNode outputNode) {
        commerciants = parser.getCommerciants();
        users = parser.getUsers();
        costs = parser.getCosts();
        output = outputNode;
        splitPayments = new ArrayList<SplitPaymentInfo>();
    }



    /**
     * method that finds a user by the corresponding email
     * @param email the email given
     * @return the corresponding user or exception if not found
     */
    public User getUserByEmail(final String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        throw new UserNotFoundException("");
    }




    /**
     * method that finds an account by the corresponding email
     * @param iban the iban given
     * @return the corresponding account or exception if not found
     */
    public Account getAccountByIBAN(final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        throw new AccountNotFoundException("");
    }



    /**
     * method that finds a user by the corresponding iban
     * @param iban the iban given
     * @return the corresponding user or exception if not found
     */
    public User getUserByIBAN(final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return user;
                }
            }
        }
        throw new UserNotFoundException("");
    }



    /**
     * method that finds an account by the corresponding iban or alias
     * @param ibanOrAlias the iban or alias given
     * @return the corresponding account or exception if not found
     */
    public Account getAccountByIBANOrAlias(final String ibanOrAlias) {
        if (ibanOrAlias.isEmpty()) {
            throw new AccountNotFoundException("");
        }
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(ibanOrAlias)
                        || account.getAlias().equals(ibanOrAlias)) {
                    return account;
                }
            }
        }
        throw new AccountNotFoundException("");
    }



    /**
     * method that finds an account by the corresponding card id
     * @param cardNumber the card id given
     * @return the corresponding account or exception if not found
     */
    public Account getAccountByCardNumber(final String cardNumber) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return account;
                    }
                }
            }
        }
        throw new AccountNotFoundException("");
    }



    /**
     * method that finds a card by the corresponding card id
     * @param cardNumber the card id given
     * @return the corresponding card or exception if not found
     */
    public Card getCardByCardNumber(final String cardNumber) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return card;
                    }
                }
            }
        }
        throw new CardNotFoundException("");
    }



    /**
     * method that finds a user by the corresponding account
     * @param account the account given
     * @return the corresponding user or exception if not found
     */
    public User getUserByAccount(final Account account) {
        for (User user : users) {
            if (user.getAccounts().contains(account)) {
                return user;
            }
        }
        throw new UserNotFoundException("");
    }



    /**
     * The method just checks the Map which contains all the costs and returns the cost
     * @param from currency from which we want to convert
     * @param to currency to which we want to convert
     * @return the conversion rate
     */
    public double findExchangeRate(final String from, final String to) {
        if (from.equals(to)) {
            return 1.0;
        } else if (costs.containsKey(new Pair(from, to))) {
            return costs.get(new Pair(from, to));
        }
        return -1;
    }



    /**
     * The method returns a commerciant based on its name
     * @param name the name of the commerciant
     * @return the commerciant instance which corresponds to the name
     */
    public Commerciant getCommerciantByName(final String name) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getCommerciant().equals(name)) {
                return commerciant;
            }
        }
        throw new CommerciantNotFoundException("");
    }



    /**
     * returns a commerciant based on its iban
     * @param iban the iban of the commerciant
     * @return the commerciant instance which corresponds to the iban
     */
    public Commerciant getCommerciantByIban(final String iban) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getAccount().equals(iban)) {
                return commerciant;
            }
        }
        throw new CommerciantNotFoundException("");
    }




    /**
     * method that gets the user's email based on an account iban
     * @param iban the account iban
     * @return the email of the user who owns the account
     */
    public String getEmailByIban(final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return user.getEmail();
                }
            }
        }
        throw new EmailNotFoundException("");
    }



    /**
     * iterates through the list of payments and returns the right one
     * @param email the email of the user
     * @param type the type of the searched payment
     * @return the SplitPaymentInfo instance which
     * corresponds to the user and type given
     */
    public SplitPaymentInfo getSplitPaymentByTypeAndEmail(final String email, final String type) {
        for (SplitPaymentInfo splitPaymentInfo : splitPayments) {
            if (splitPaymentInfo.isRightType(type, email)
                    && !splitPaymentInfo.getObserver().hasAcceptedAllHisAccounts(email)) {
                return splitPaymentInfo;
            }
        }
        throw new PaymentInfoNotFoundException("");
    }

}

package org.poo.bankGraph;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.ExchangeRate;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.InputParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

@Getter
@Setter
/**
 * Singleton class designed to have the main classes as fields for all the operations
 */
public final class Bank {
    private ArrayList<Commerciant> commerciants;
    private Map<String, List<Edge>> exchangeRates;
    private ArrayList<User> users;
    private ArrayList<ExchangeRate> exchangeRatesList;
    private ArrayNode output;
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
        exchangeRates = parser.getGraph();
        users = parser.getUsers();
        exchangeRatesList = parser.getExchangeRatesList();
        output = outputNode;
    }

    /**
     * method that finds a user by the corresponding email
     * @param email
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
     * @param iban
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
     * @param iban
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
     * @param ibanOrAlias
     * @return the corresponding account or exception if not found
     */
    public Account getAccountByIBANOrAlias(final String ibanOrAlias) {
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
     * @param cardNumber
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
     * @param cardNumber
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
     * @param account
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
     * The method iterates over the exchangeRates that were previously added in O(n) complexity
     * If it isn't found like that, it runs dfs and adds all the nodes which are not contained into
     * the found exchange rates
     * @param from currency from which we want to convert
     * @param to currency to which we want to convert
     * @return the conversion rate
     */
    public double findExchangeRate(final String from, final String to) {
        for (ExchangeRate exchangeRate : exchangeRatesList) {
            if (exchangeRate.getFrom().equals(from) && exchangeRate.getTo().equals(to)) {
                return exchangeRate.getRate();
            }
        }
        Queue<Pair<String, Double>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(new Pair<>(from, 1.0));
        while (!queue.isEmpty()) {
            Pair<String, Double> current = queue.poll();
            String currentCurrency = current.getKey();
            double currentRate = current.getValue();

            if (!from.equals(currentCurrency)) {
                ExchangeRate exchangeRate = new ExchangeRate(from, currentRate, currentCurrency);
                ExchangeRate reverseExchangeRate = new ExchangeRate(exchangeRate);

                if (!exchangeRatesList.contains(reverseExchangeRate)) {
                    exchangeRatesList.add(reverseExchangeRate);
                    exchangeRatesList.add(exchangeRate);
                }
            }


            if (currentCurrency.equals(to)) {
                ExchangeRate exchangeRate = new ExchangeRate(from, currentRate, to);
                exchangeRatesList.add(exchangeRate);
                exchangeRatesList.add(new ExchangeRate(exchangeRate));
                return currentRate;
            }

            if (!visited.contains(currentCurrency)) {
                visited.add(currentCurrency);
                List<Edge> neighbors;
                if (exchangeRates.containsKey(currentCurrency)) {
                    neighbors = exchangeRates.get(currentCurrency);
                } else {
                    neighbors = Collections.emptyList();
                }
                for (Edge edge : neighbors) {
                    queue.add(new Pair<>(edge.getTo(), currentRate * edge.getRate()));
                }
            }
        }
        return -1;
    }

}

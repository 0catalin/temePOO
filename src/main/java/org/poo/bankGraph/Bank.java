package org.poo.bankGraph;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.ExchangeRate;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.parsers.InputParser;

import java.util.*;

@Getter
@Setter
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

    public void applyParams(final InputParser parser, final ArrayNode finalNode) {
        commerciants = parser.getCommerciants();
        exchangeRates = parser.getGraph();
        users = parser.getUsers();
        exchangeRatesList = parser.getExchangeRatesList();
        output = finalNode;
    }

    public User getUserByEmail(final String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public Account getAccountByIBAN(final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    public User getUserByIBAN(final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return user;
                }
            }
        }
        return null;
    }

    public Account getAccountByIBANOrAlias(final String ibanOrAlias) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(ibanOrAlias)
                        || account.getAlias().equals(ibanOrAlias)) {
                    return account;
                }
            }
        }
        return null;
    }

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
        return null;
    }

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
        return null;
    }

    public User getUserByAccount(final Account account) {
        for (User user : users) {
            if (user.getAccounts().contains(account)) {
                return user;
            }
        }
        return null;
    }


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

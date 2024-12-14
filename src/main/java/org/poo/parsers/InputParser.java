package org.poo.parsers;

import org.poo.bankPair.Pair;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.ExchangeRate;
import org.poo.baseinput.User;
import org.poo.parsers.commands.CommandFactory;

import org.poo.parsers.commands.Command;
import lombok.Getter;
import lombok.Setter;
import org.poo.parsers.fileio.CommerciantInput;
import org.poo.parsers.fileio.ExchangeInput;
import org.poo.parsers.fileio.ObjectInput;
import org.poo.parsers.fileio.UserInput;
import org.poo.parsers.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter

/**
 * class used for parsing the input and storing it in objects
 */
public final class InputParser {
        private ArrayList<Command> commands;
        private ArrayList<Commerciant> commerciants;
        private Map<Pair, Double> costs;
        private ArrayList<User> users;
        private ArrayList<ExchangeRate> exchangeRatesList;

        public InputParser(final ObjectInput input) {
            commands = new ArrayList<Command>();
            commerciants = new ArrayList<Commerciant>();
            costs = new HashMap<Pair, Double>();
            users = new ArrayList<User>();
            exchangeRatesList = new ArrayList<ExchangeRate>();


            for (UserInput userInput : input.getUsers()) {
                users.add(new User(userInput));
            }


            for (ExchangeInput exchangeInput : input.getExchangeRates()) {
                ExchangeRate exchangeRate = new ExchangeRate(exchangeInput);
                exchangeRatesList.add(exchangeRate);
                exchangeRatesList.add(new ExchangeRate(exchangeRate));
                costs.putIfAbsent(new Pair(exchangeInput.getFrom(), exchangeInput.getTo()),
                        exchangeInput.getRate());
                costs.putIfAbsent(new Pair(exchangeInput.getTo(), exchangeInput.getFrom()),
                        1 / exchangeInput.getRate());
            }


            if (input.getCommerciants() != null) {
                for (CommerciantInput commerciantInput : input.getCommerciants()) {
                    commerciants.add(new Commerciant(commerciantInput));
                }
            }


            Set<String> currencies = new HashSet<String>();
            for (ExchangeInput exchangeInput : input.getExchangeRates()) {
                currencies.add(exchangeInput.getFrom());
                currencies.add(exchangeInput.getTo());
            }
            for (String from : currencies) {
                for (String to : currencies) {
                    if (!from.equals(to) && !costs.containsKey(new Pair(from, to))) {
                        Set<String> visited = new HashSet<String>();
                        double cost = getRate(from, from, to, visited, 1.0);
                        costs.put(new Pair(from, to), cost);
                        costs.put(new Pair(to, from), 1 / cost);
                    }
                }
            }


            for (CommandInput commandInput : input.getCommands()) {
                commands.add(CommandFactory.createCommand(commandInput));
            }
        }



    /**
     * recursive method to find the exchange rate between two currencies
     * @param fromInitial initial cost to be stored for every function call
     * @param from the from currency
     * @param to the to currency
     * @param visited an ArrayList to keep track of the visited nodes
     * @param currentRate the current calculated rate
     * @return the exchange rate between the 2 currencies
     */
    private double getRate(final String fromInitial, final String from,
                           final String to, final Set<String> visited, final double currentRate) {
        if (visited.contains(from)) {
            return -1;
        }
        visited.add(from);
        for (ExchangeRate exchangeRate : exchangeRatesList) {
            if (exchangeRate.getFrom().equals(from)) {
                String next = exchangeRate.getTo();
                double rate = exchangeRate.getRate();
                costs.putIfAbsent(new Pair(fromInitial, next), rate * currentRate);
                costs.putIfAbsent(new Pair(next, fromInitial), 1 / rate / currentRate);
                if (next.equals(to)) {
                    return currentRate * rate;
                }
                double cost = getRate(fromInitial, next, to, visited, currentRate * rate);
                if (cost != -1) {
                    return cost;
                }
            }
        }
        return -1;
    }

}



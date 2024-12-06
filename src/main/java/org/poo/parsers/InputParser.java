package org.poo.parsers;

import org.poo.bankGraph.Edge;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.ExchangeRate;
import org.poo.baseinput.User;
import org.poo.commands.CommandFactory;
import org.poo.fileio.*;
import org.poo.commands.Command;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter

    public final class InputParser {
        private ArrayList<Command> commands;
        private ArrayList<Commerciant> commerciants;
        private Map<String, List<Edge>> graph;
        private ArrayList<User> users;
        private ArrayList<ExchangeRate> exchangeRatesList;

        public InputParser(final ObjectInput input) {
            commands = new ArrayList<>();
            commerciants = new ArrayList<>();
            graph = new HashMap<>();
            users = new ArrayList<>();
            exchangeRatesList = new ArrayList<>();
            for (UserInput userInput : input.getUsers()) {
                users.add(new User(userInput));
            }
            for (ExchangeInput exchangeInput : input.getExchangeRates()) {
                ExchangeRate exchangeRate = new ExchangeRate(exchangeInput);
                exchangeRatesList.add(exchangeRate);
                exchangeRatesList.add(new ExchangeRate(exchangeRate));
            }
            if (input.getCommerciants() != null) {
                for (CommerciantInput commerciantInput : input.getCommerciants()) {
                    commerciants.add(new Commerciant(commerciantInput));
                }
            }
            for (ExchangeInput exchangeInput : input.getExchangeRates()) {
                String from = exchangeInput.getFrom();
                String to = exchangeInput.getTo();
                double rate = exchangeInput.getRate();
                double oppositeRate = 1 / rate;
                if (!graph.containsKey(from)) {
                    graph.put(from, new ArrayList<>());
                }
                graph.get(from).add(new Edge(to, rate));
                if (!graph.containsKey(to)) {
                    graph.put(to, new ArrayList<>());
                }
                graph.get(to).add(new Edge(from, oppositeRate));
            }
            for (CommandInput commandInput : input.getCommands()) {
                commands.add(CommandFactory.createCommand(commandInput));
            }
        }





}



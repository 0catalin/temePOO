package org.poo.parsers;

import org.poo.baseinput.Commerciant;
import org.poo.baseinput.ExchangeRate;
import org.poo.baseinput.User;
import org.poo.commands.CommandFactory;
import org.poo.fileio.*;
import org.poo.commands.Command;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter

public class InputParser {
    private ArrayList<Command> commands;
    private ArrayList<Commerciant> commerciants;
    private ArrayList<ExchangeRate> exchangeRates;
    private ArrayList<User> users;

    public InputParser(ObjectInput input) {
        commands = new ArrayList<>();
        commerciants = new ArrayList<>();
        exchangeRates = new ArrayList<>();
        users = new ArrayList<>();
        for (UserInput userInput : input.getUsers()) {
            users.add(new User(userInput));
        }
        if (input.getCommerciants() != null) {
            for (CommerciantInput commerciantInput : input.getCommerciants()) {
                commerciants.add(new Commerciant(commerciantInput));
            }
        }
        for(ExchangeInput exchangeInput : input.getExchangeRates()) {
            ExchangeRate exchangeRate = new ExchangeRate(exchangeInput);
            exchangeRates.add(exchangeRate);
            exchangeRates.add(new ExchangeRate(exchangeRate));
        }
        for (CommandInput commandInput : input.getCommands()) {
            commands.add(CommandFactory.createCommand(commandInput));
        }
    }
}

package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.baseinput.Commerciant;
import org.poo.baseinput.ExchangeRate;
import org.poo.baseinput.User;
import org.poo.parsers.InputParser;

import java.util.ArrayList;

@Getter
@Setter
public class Bank {
    private ArrayList<Commerciant> commerciants;
    private ArrayList<ExchangeRate> exchangeRates;
    private ArrayList<User> users;

    private static Bank instance = null;
    static {
        instance = new Bank();
    }
    public static Bank getInstance() {
        return instance;
    }
    private Bank() { }

    public void applyParams(InputParser parser) {
        commerciants = parser.getCommerciants();
        exchangeRates = parser.getExchangeRates();
        users = parser.getUsers();
    }


}

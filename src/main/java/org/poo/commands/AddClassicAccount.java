package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

public class AddClassicAccount implements Command{
    private String email;
    private String currency;
    private int timestamp;
    public AddClassicAccount(CommandInput commandInput) {
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        ClassicAccount classicAccount = new ClassicAccount(currency);
        if(bank.getUserByEmail(email) != null) {
            bank.getUserByEmail(email).getAccounts().add(classicAccount);
        } else {
            System.out.println("User Not found"); // de modificat daca nu e vreo problema
        }

    }
}

package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.ClassicAccount;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

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
            User user = bank.getUserByEmail(email);
            user.getAccounts().add(classicAccount);
            user.getTranzactions().computeIfAbsent(addToUsersTranzactions(mapper), k -> new ArrayList<>()).add(classicAccount.getIBAN());

        } else {
            System.out.println("User Not found"); // de modificat daca nu e vreo problema
        }

    }

    private ObjectNode addToUsersTranzactions(ObjectMapper mapper) {
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New account created");
        return output;
    }
}

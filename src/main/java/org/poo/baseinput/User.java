package org.poo.baseinput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.fileio.UserInput;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private Map<ObjectNode, List<String>> tranzactions;
    private ArrayList<Account> accounts; // must have the cards

    public User(UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
        tranzactions = new LinkedHashMap<>();
        accounts = new ArrayList<>();
    }



}

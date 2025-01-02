package org.poo.baseinput;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.parsers.fileio.UserInput;


import java.util.ArrayList;

@Getter
@Setter

/**
 * class designed to store an input user
 */

public final class User {

    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<ObjectNode> tranzactions;
    private ArrayList<Account> accounts;

    public User(final UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
        tranzactions = new ArrayList<ObjectNode>();
        accounts = new ArrayList<Account>();
    }
}

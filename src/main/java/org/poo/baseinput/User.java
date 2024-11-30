package org.poo.baseinput;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.fileio.UserInput;
import org.poo.tranzactions.Tranzaction;

import java.util.ArrayList;

@Getter
@Setter

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Tranzaction> tranzactions;
    private ArrayList<Account> accounts; // must have the cards

    public User(UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
        tranzactions = new ArrayList<>();
        accounts = new ArrayList<>();
    }



}

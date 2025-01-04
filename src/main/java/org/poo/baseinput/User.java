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
    private String birthDate;
    private String occupation;
    private String servicePlan;
    private ArrayList<ObjectNode> tranzactions;
    private ArrayList<Account> accounts;
    private ArrayList<Account> classicAccounts;

    public User(final UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
        birthDate = userInput.getBirthDate();
        occupation = userInput.getOccupation();
        tranzactions = new ArrayList<ObjectNode>();
        accounts = new ArrayList<Account>();
        classicAccounts = new ArrayList<>();
        if (occupation.equals("student")) {
            servicePlan = "student";
        } else {
            servicePlan = "standard";
        }

    }

    public boolean isStudent() {
        return occupation.equals("student");
    }


    public double getPlanMultiplier (double amount) {
        if (servicePlan.equals("student") || servicePlan.equals("gold")) {
            return 1;
        } else if (servicePlan.equals("standard")) {
            return 1.02;
        } else if (servicePlan.equals("silver")) {
            if (amount < 500) {
                return 1;
            } else {
                return 1.01;
            }
        }
        return 10000;

    }

    public boolean isUserOldEnough() {
        String[] parts = birthDate.split("-");
        int birthYear = Integer.parseInt(parts[0]);
        int birthMonth = Integer.parseInt(parts[1]);
        int birthDay = Integer.parseInt(parts[2]);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int currentYear = calendar.get(java.util.Calendar.YEAR);
        int currentMonth = calendar.get(java.util.Calendar.MONTH) + 1;
        int currentDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);


        if (currentYear - birthYear > 21) {
            return true;
        } else if (currentYear - birthYear < 21) {
            return false;
        }

        if (currentMonth > birthMonth) {
            return true;
        } else if (currentMonth < birthMonth) {
            return false;
        }

        if (currentDay > birthDay) {
            return true;
        } else if (currentDay < birthDay) {
            return false;
        }
        return true;

    }


}

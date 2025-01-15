package org.poo.baseinput;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.accounts.cards.Card;
import org.poo.exceptions.CardNotFoundException;
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
    private int numberOfPaymentsForGold;

    private static final double STANDARD_TAX = 1.002;
    private static final double SILVER_TAX = 1.001;
    private static final double SILVER_TAX_LIMIT = 500.0;
    private static final int MINIMUM_AGE = 21;
    private static final double FIVE_PAYMENTS_LIMIT = 300.0;
    private static final double TOTAL_PAYMENTS_LIMIT = 5;



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
        numberOfPaymentsForGold = 0;
    }


    /**
     * function which checks for the occupation of the user
     * @return true if he is a student and false otherwise
     */
    public boolean isStudent() {
        return occupation.equals("student");
    }


    /**
     * gets the plan multiplier of the user depending on the amount
     * @param amount the amount of the payment being done
     * @return the multiplier of the amount having to be paid
     */
    public double getPlanMultiplier(final double amount) {
        if (servicePlan.equals("student") || servicePlan.equals("gold")) {
            return 1;
        } else if (servicePlan.equals("standard")) {
            return STANDARD_TAX;
        } else if (amount < SILVER_TAX_LIMIT) {
            return 1;
        } else {
            return SILVER_TAX;
        }
    }


    /**
     * calculates whether the user is over 21 or not
     * @return true if he is more than 21 and false otherwise
     */
    public boolean isUserOldEnough() {
        String[] parts = birthDate.split("-");
        int birthYear = Integer.parseInt(parts[0]);
        int birthMonth = Integer.parseInt(parts[1]);
        int birthDay = Integer.parseInt(parts[2]);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int currentYear = calendar.get(java.util.Calendar.YEAR);
        int currentMonth = calendar.get(java.util.Calendar.MONTH) + 1;
        int currentDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);


        if (currentYear - birthYear > MINIMUM_AGE) {
            return true;
        } else if (currentYear - birthYear < MINIMUM_AGE) {
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


    /**
     * function that updates fields depending on the payment amounts and
     * the total number of payments and changes the user's plan to gold
     * after 5 payments of at least 300 RON each
     * @param amount amount being spent by the user
     * @param iban the iban of the user
     * @param timestamp the timestamp of the payment
     */
    public void checkFivePayments(final double amount, final String iban, final int timestamp) {
        if (servicePlan.equals("silver") && amount >= FIVE_PAYMENTS_LIMIT) {
            numberOfPaymentsForGold++;
        }
        if (numberOfPaymentsForGold == TOTAL_PAYMENTS_LIMIT) {
            numberOfPaymentsForGold++;
            servicePlan = "gold";
            tranzactions.add(addGoldUpgrade(iban, timestamp));
        }
    }



    private ObjectNode addGoldUpgrade(final String iban, final int timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "Upgrade plan");
        output.put("accountIBAN", iban);
        output.put("newPlanType", "gold");
        return output;
    }


    /**
     * returns the user's card by the card number given
     * @param cardNumber given card number of user
     * @return the corresponding card object
     */
    public Card getCardByCardNumber(final String cardNumber) {
        for (Account account : accounts) {
            for (Card card : account.getCards()) {
                if (card.getCardNumber().equals(cardNumber)) {
                    return card;
                }
            }
        }
        throw new CardNotFoundException("");
    }


}

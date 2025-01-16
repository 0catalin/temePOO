package org.poo.splitPayment;

import org.poo.bankPair.Bank;
import org.poo.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.List;



/**
 * class that monitors the users' acceptance of the split payments
 */
public final class PayAllObserver {
    // hashmap with all the iban used for split and a boolean
    // indicating if the accept was made or not
    private final HashMap<String, Boolean> acceptMap;


    public PayAllObserver(final List<String> accountsForSplit) {
        acceptMap = new HashMap<String, Boolean>();
        for (String iban : accountsForSplit) {
            acceptMap.put(iban, false);
        }
    }


    /**
     * function that accepts the email and then checks if all the users have accepted
     * @param email the email of the user
     * @return true if all have accepted, else false
     */
    public boolean update(final String email) {
        acceptValue(email);
        for (boolean accept : acceptMap.values()) {
            if (!accept) {
                return false;
            }
        }
        return true;
    }




    /**
     * places true for the email (simulating an accept) in the hashMap
     * @param email the user email
     */
    private void acceptValue(final String email) {
        for (String iban : acceptMap.keySet()) {
            try {
                if (Bank.getInstance().getUserByIBAN(iban).getEmail().equals(email)
                        && !acceptMap.get(iban)) {
                    acceptMap.put(iban, true);
                    break;
                }
            } catch (UserNotFoundException ignored) {

            }
        }

    }


    /**
     * function which checks if a user has accepted all his accounts
     * to know if the accept is on the current instance fom the arrayList
     * or the next
     * @param email the user email
     * @return true he has previously accepted all and false otherwise
     */
    public boolean hasAcceptedAllHisAccounts(final String email) {
        for (String iban : acceptMap.keySet()) {
            try {
                if (Bank.getInstance().getUserByIBAN(iban).getEmail().equals(email)
                        && !acceptMap.get(iban)) {
                    return false;
                }

            } catch (UserNotFoundException ignored) {
                return true;
            }
        }
        return true;
    }
}

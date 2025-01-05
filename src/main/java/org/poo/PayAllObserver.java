package org.poo;

import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.List;

public class PayAllObserver {
    HashMap<String, Boolean> acceptMap;
    public PayAllObserver(List<String> accountsForSplit) {
        acceptMap = new HashMap<String, Boolean>();
        for (String iban : accountsForSplit) {
            acceptMap.put(iban, false);
        }
    }

    public boolean update(String email) {
        acceptValue(email);
        for (boolean accept : acceptMap.values()) {
            if (!accept)
                return false;
        }
        return true;
    }

    private void acceptValue(String email) {
        for (String iban : acceptMap.keySet()) {
            try {
                if (Bank.getInstance().getUserByIBAN(iban).getEmail().equals(email)) {
                    acceptMap.put(iban, true);
                    break;
                }
            } catch (UserNotFoundException ignored) {

            }
        }

    }
}

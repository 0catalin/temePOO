package org.poo;

import org.poo.bankPair.Bank;
import org.poo.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.List;

public final class PayAllObserver {

    private HashMap<String, Boolean> acceptMap;


    public PayAllObserver(final List<String> accountsForSplit) {
        acceptMap = new HashMap<String, Boolean>();
        for (String iban : accountsForSplit) {
            acceptMap.put(iban, false);
        }
    }



    public boolean update(final String email) {
        acceptValue(email);
        for (boolean accept : acceptMap.values()) {
            if (!accept) {
                return false;
            }
        }
        return true;
    }


    // daca userul are contul nu e suficient, trebuie si sa nu fie acceptat de dinainte
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

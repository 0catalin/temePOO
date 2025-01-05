package org.poo;

import java.util.HashMap;
import java.util.List;

public class PayAllObserver {
    HashMap<String, Boolean> acceptMap;
    public PayAllObserver(List<String> accountsForSplit) {
        acceptMap = new HashMap<String, Boolean>();
        for (String account : accountsForSplit) {
            acceptMap.put(account, false);
        }
    }

    public boolean checkForAll(String user) {
        acceptValue(user);
        for (boolean accept : acceptMap.values()) {
            if (!accept)
                return false;
        }
        return true;
    }

    private void acceptValue(String user) {
        acceptMap.put(user, true);
    }
}

package org.poo.characters.cards;

import org.poo.characters.Minion;

public abstract class MinionFactory {
    /**
     * takes minion as param, creates another minion based on name and copies all the fields
     * to the new minion created, returning the new minion created
     * @param minion the minion based on which the factory will create the specific minion
     * @return the specific minion
     */
    public static Minion createMinion(final Minion minion) {
        if (minion.getName().equals("Disciple")) {
            return new Disciple(minion);
        }
        if (minion.getName().equals("Miraj")) {
            return new Miraj(minion);
        }
        if (minion.getName().equals("The Cursed One")) {
            return new TheCursedOne(minion);
        }
        if (minion.getName().equals("The Ripper")) {
            return new TheRipper(minion);
        }
        return minion;
    }
}

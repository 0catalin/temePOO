package org.poo.characters.cards;

import org.poo.characters.Minion;
/**
 * The Miraj class is simply a subclass of the Minion class used in MinionFactory
 */
public final class Miraj extends Minion {
    public Miraj(final Minion minion) {
        setHealth(minion.getHealth());
        setColors(minion.getColors());
        setName(minion.getName());
        setAttackDamage(minion.getAttackDamage());
        setIsFrozen(minion.getIsFrozen());
        setDescription(minion.getDescription());
        setMana(minion.getMana());
        setHasAttacked(minion.getHasAttacked());
    }
    /*
    implements Miraj special attack, swaps his health with the attacked's health
    */
    @Override
    public void specialAttack(final Minion attacked) {
        int swap = attacked.getHealth();
        attacked.setHealth(getHealth());
        setHealth(swap);
    }
}

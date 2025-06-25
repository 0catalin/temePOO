package org.poo.characters.cards;

import org.poo.characters.Minion;
/**
 * The Disciple class is simply a subclass of the Minion class used in MinionFactory
 */
public final class Disciple extends Minion {
    public Disciple(final Minion minion) {
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
    implements Disciple special attack, increases attacked's health by 2
    */
    @Override
    public void specialAttack(final Minion attacked) {
        attacked.setHealth(attacked.getHealth() + 2);
    }
}

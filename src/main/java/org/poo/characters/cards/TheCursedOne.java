package org.poo.characters.cards;

import org.poo.characters.Minion;
/**
 * The TheCursedOne class is simply a subclass of the Minion class used in MinionFactory
 */
public final class TheCursedOne extends Minion {
    public TheCursedOne(final Minion minion) {
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
    implements The Cursed One special attack, swaps attacked's attack damage with its health
    */
    @Override
    public void specialAttack(final Minion attacked) {
        int swap = attacked.getHealth();
        attacked.setHealth(attacked.getAttackDamage());
        attacked.setAttackDamage(swap);
    }
}

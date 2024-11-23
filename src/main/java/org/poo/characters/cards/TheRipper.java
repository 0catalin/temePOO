package org.poo.characters.cards;

import org.poo.characters.Minion;
/**
 * The TheRipper class is simply a subclass of the Minion class used in MinionFactory
 */
public final class TheRipper extends Minion {
    public TheRipper(final Minion minion) {
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
    implements TheRipper special attack, decreases the attacked attack damage by 2
    */
    @Override
    public void specialAttack(final Minion attacked) {
        attacked.setAttackDamage(attacked.getAttackDamage() - 2);
        if (attacked.getAttackDamage() < 0) {
            attacked.setAttackDamage(0);
        }
    }
}

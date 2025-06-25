package org.poo.characters.heroes;

import org.poo.characters.Hero;
import org.poo.characters.Minion;
import org.poo.game.TableCards;

import java.util.ArrayList;
/**
 * The LordRoyce class is simply a subclass of the Hero class used in HeroFactory
 */
public final class LordRoyce extends Hero {
    public LordRoyce() { }
    public LordRoyce(final Hero hero) {
        setColors(hero.getColors());
        setName(hero.getName());
        setDescription(hero.getDescription());
        setHealth(hero.getHealth());
        setHasAttacked(hero.getHasAttacked());
        setMana(hero.getMana());
    }
    /*
    implements subZero attack, iterates over a row and freezes everything
    */
    @Override
    public void specialAttack(final int affectedRow, final TableCards tableCards) {
        ArrayList<Minion> row = tableCards.getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setIsFrozen(true);
        }
    }
}

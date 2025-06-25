package org.poo.characters.heroes;

import org.poo.characters.Hero;
import org.poo.characters.Minion;
import org.poo.game.TableCards;

import java.util.ArrayList;
/**
 * The EmpressThorina class is simply a subclass of the Hero class used in HeroFactory
 */
public final class EmpressThorina extends Hero {
    public EmpressThorina() { }
    public EmpressThorina(final Hero hero) {
        setHealth(hero.getHealth());
        setColors(hero.getColors());
        setName(hero.getName());
        setDescription(hero.getDescription());
        setHasAttacked(hero.getHasAttacked());
        setMana(hero.getMana());
    }
    /*
    implements lowBlow attack, eliminates the minion with the maximum health
    */
    @Override
    public void specialAttack(final int affectedRow, final TableCards tableCards) {
        ArrayList<Minion> row = tableCards.getRow(affectedRow);
        Minion maxHealthMinion = new Minion();
        int maxHealthIndex = 0;
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i).getHealth() > maxHealthMinion.getHealth()) {
                maxHealthMinion = row.get(i);
                maxHealthIndex = i;
            }
        }
        row.remove(maxHealthIndex);
    }
}

package org.poo.characters.heroes;

import org.poo.characters.Hero;
import org.poo.characters.Minion;
import org.poo.game.TableCards;

import java.util.ArrayList;
/**
 * The GeneralKocioraw class is simply a subclass of the Hero class used in HeroFactory
 */
public final class GeneralKocioraw extends Hero {
    public GeneralKocioraw() { }
    public GeneralKocioraw(final Hero hero) {
        setName(hero.getName());
        setColors(hero.getColors());
        setDescription(hero.getDescription());
        setMana(hero.getMana());
        setHasAttacked(hero.getHasAttacked());
        setHealth(hero.getHealth());
    }
    /*
    implements BloodThirst attack, iterates over a row and increases attackDamage
    */
    @Override
    public void specialAttack(final int affectedRow, final TableCards tableCards) {
        ArrayList<Minion> row = tableCards.getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setAttackDamage(row.get(i).getAttackDamage() + 1);
        }
    }


}

package org.poo.characters.heroes;

import org.poo.characters.Hero;
import org.poo.characters.Minion;
import org.poo.game.TableCards;

import java.util.ArrayList;
/**
 * The KingMudface class is simply a subclass of the Hero class used in HeroFactory
 */
public final class KingMudface extends Hero {
    public KingMudface() { }
    public KingMudface(final Hero hero) {
        setColors(hero.getColors());
        setName(hero.getName());
        setDescription(hero.getDescription());
        setMana(hero.getMana());
        setHealth(hero.getHealth());
        setHasAttacked(hero.getHasAttacked());
    }
    /*
    implements EarthBorn attack, iterates over a row and increases health
    */
    @Override
    public void specialAttack(final int affectedRow, final TableCards tableCards) {
        ArrayList<Minion> row = tableCards.getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setHealth(row.get(i).getHealth() + 1);
        }
    }
    }


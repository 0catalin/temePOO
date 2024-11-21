package org.poo.characters.heroes;

import org.poo.characters.Hero;

/**
 * The HeroFactory class is a class used for creating specific Hero inheriting classes
 */
public final class HeroFactory {
    public HeroFactory() {
    }

    /**
     * takes hero as param, creates another hero based on name and copies all the fields
     * to the new hero created, returning the new hero created
     * @param hero the hero based on which the factory will create the specific hero
     * @return the specific hero
     */
    public Hero createHero(final Hero hero) {
        if (hero.getName().equals("Empress Thorina")) {
            return new EmpressThorina(hero);
        }
        if (hero.getName().equals("Lord Royce")) {
            return new LordRoyce(hero);
        }
        if (hero.getName().equals("General Kocioraw")) {
            return new GeneralKocioraw(hero);
        }
        if (hero.getName().equals("King Mudface")) {
            return new KingMudface(hero);
        }
        return null;
    }
}


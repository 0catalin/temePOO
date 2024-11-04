package main;

import fileio.DecksInput;

public final class Player {
    private int mana;
    private Decks decks;
    private Hero hero;
    public Player(final DecksInput decks) {
        this.mana = 0;
        this.decks = new Decks(decks);
    }

    public int getMana() {
        return mana;
    }

    public Decks getDecks() {
        return decks;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public void setDecks(final Decks decks) {
        this.decks = decks;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }
}


package main;

import fileio.DecksInput;

public class Player {
    private int mana;
    private Decks decks;
    private Hero hero;
    public Player(DecksInput decks) {
        this.mana = 0;
        this.decks = new Decks(decks);
    }

    void increaseMana(int mana) {
        this.mana += mana;
    }
    public int getMana() {return mana;}
    public Decks getDecks() {return decks;}
    void setMana(int mana) {this.mana = mana;}
    void setDecks(Decks decks) {this.decks = decks;}
    public Hero getHero() {return hero;}
    public void setHero(Hero hero) {this.hero = hero;}
}


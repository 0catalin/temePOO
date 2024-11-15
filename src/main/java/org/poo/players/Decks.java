package org.poo.players;
import org.poo.characters.Minion;
import org.poo.fileio.DecksInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The Decks class is designed for constructing the decks
 * from scratch with the JSON Input and storing them. Its fields are the
 * number of cards, decks and an arraylist of an arraylist with Minions
 */
public final class Decks {
    private int numberOfCards;
    private int numberOfDecks;
    private ArrayList<ArrayList<Minion>> decks = new ArrayList<ArrayList<Minion>>();
    public Decks(final DecksInput decksInput) {
        this.numberOfCards = decksInput.getNrCardsInDeck();
        this.numberOfDecks = decksInput.getNrDecks();
        for (int i = 0; i < numberOfDecks; i++) {
            ArrayList<Minion> deck = new ArrayList<Minion>();
            for (int j = 0; j < numberOfCards; j++) {
                deck.add(new Minion(decksInput.getDecks().get(i).get(j)));
            }
            decks.add(deck);
        }
    }
    public int getNumberOfCards() {
        return numberOfCards;
    }
    public int getNumberOfDecks() {
        return numberOfDecks;
    }
    public void setNumberOfCards(final int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }
    public void setNumberOfDecks(final int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }
    public void setDecks(final ArrayList<ArrayList<Minion>> decks) {
        this.decks = decks;
    }
    public ArrayList<ArrayList<Minion>> getDecks() {
        return decks;
    }

    /**
     * Shuffles the deck corresponding to the specified deck ID, using a seed for reproducibility.
     * @param deckID the index for the deck
     * @param seed the seed used to randomize
     */
    public void shuffleDeck(final int deckID, final int seed) {
        ArrayList<Minion> deck = decks.get(deckID);
        Random random = new Random(seed);
        Collections.shuffle(deck, random);
    }
}

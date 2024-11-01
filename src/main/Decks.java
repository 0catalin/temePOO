package main;
import fileio.DecksInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Decks {
    private int numberOfCards;
    private int numberOfDecks;
    private ArrayList<ArrayList<Minion>> decks = new ArrayList<>();
    public Decks(DecksInput decksInput) {
        this.numberOfCards = decksInput.getNrCardsInDeck();
        this.numberOfDecks = decksInput.getNrDecks();
        for(int i = 0; i < numberOfDecks; i++) {
            ArrayList<Minion> deck = new ArrayList<>();
            for(int j = 0; j < numberOfCards; j++) {
                deck.add(new Minion(decksInput.getDecks().get(i).get(j)));
            }
            decks.add(deck);
        }
    }
    int getNumberOfCards() {return numberOfCards;}
    int getNumberOfDecks() {return numberOfDecks;}
    void setNumberOfCards(int numberOfCards) {this.numberOfCards = numberOfCards;}
    void setNumberOfDecks(int numberOfDecks) {this.numberOfDecks = numberOfDecks;}
    void setDecks(ArrayList<ArrayList<Minion>> decks) {this.decks = decks;}
    ArrayList<ArrayList<Minion>> getDecks() {return decks;}

    void shuffleDeck(int deckID, int seed) {
        ArrayList<Minion> deck = decks.get(deckID);
        Random random = new Random(seed);
        Collections.shuffle(deck, random);
    }
}

package main;

import java.util.ArrayList;

/**
 * the Game info contains the decks, hands of the players and some
 * other relevant information for the game to function properly
 */
public final class GameInfo {
    private ArrayList<Minion> deckPlayer1;
    private ArrayList<Minion> deckPlayer2;
    private ArrayList<Minion> handPlayer1;
    private ArrayList<Minion> handPlayer2;
    private int playerTurn;
    private int roundNumber;
    private boolean isANewTurn;
    private static final int MAX_ROW_ELEMENTS = 5;
    private static final int LAST_ROW = 3;
    private static final int MAXIMUM_RECEIVED_MANA = 10;
    public GameInfo() {
    }

    /**
     * sets up the start of a new round :
     * gives the players the mana,
     * simulates the players taking a card from their deck
     * @param player1 the first player
     * @param player2 the second player
     */
    public void setupStartRound(final Player player1, final Player player2) {
        player1.setMana(player1.getMana() + receiveMana());
        player2.setMana(player2.getMana() + receiveMana());
        if (deckPlayer1.size() >= 1) {
            handPlayer1.add(deckPlayer1.get(0));
            deckPlayer1.remove(0);
        }
        if (deckPlayer2.size() >= 1) {
            handPlayer2.add(deckPlayer2.get(0));
            deckPlayer2.remove(0);
        }
    }

    /**
     * the function simulates adding a card from the hand
     * of a player to the table
     * @param player the player adding the card
     * @param tableCards the table where the card is added
     * @param cardIdx the index of the card in a player's hand
     * @return an empty string on success and an error string on failure
     */
    public String addCardToTable(final Player player, final TableCards tableCards,
                                 final int cardIdx) {
        Minion cardToMove = null;
        if (playerTurn == 2) {
            if (handPlayer2.size() > cardIdx) {
                cardToMove = handPlayer2.get(cardIdx);
            } else {
                return "Not enough cards in hand";
            }
            if (player.getMana() < cardToMove.getMana()) {
                return "Not enough mana to place card on table.";
            }
            if (cardToMove.getRow().equals("back")) {
                if (tableCards.getRow(0).size() == MAX_ROW_ELEMENTS) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(0).add(cardToMove);
            }
            if (cardToMove.getRow().equals("front")) {
                if (tableCards.getRow(1).size() == MAX_ROW_ELEMENTS) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(1).add(cardToMove);
            }
            handPlayer2.remove(cardIdx);
            player.setMana(player.getMana() - cardToMove.getMana());
        } else {
            if (handPlayer1.size() > cardIdx) {
                cardToMove = handPlayer1.get(cardIdx);
            } else {
                return "Not enough cards in hand";
            }
            if (player.getMana() < cardToMove.getMana()) {
                return "Not enough mana to place card on table.";
            }
            if (cardToMove.getRow().equals("back")) {
                if (tableCards.getRow(LAST_ROW).size() == MAX_ROW_ELEMENTS) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(LAST_ROW).add(cardToMove);
            }
            if (cardToMove.getRow().equals("front")) {
                if (tableCards.getRow(2).size() == MAX_ROW_ELEMENTS) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(2).add(cardToMove);
            }
            handPlayer1.remove(cardIdx);
            player.setMana(player.getMana() - cardToMove.getMana());
        }
        return "";
    }

    private int receiveMana() {
        if (roundNumber <= MAXIMUM_RECEIVED_MANA) {
            return roundNumber;
        }
        return MAXIMUM_RECEIVED_MANA;
    }
    public ArrayList<Minion> getDeckPlayer1() {
        return deckPlayer1;
    }
    public ArrayList<Minion> getHandPlayer1() {
        return handPlayer1;
    }
    public ArrayList<Minion> getHandPlayer2() {
        return handPlayer2;
    }
    public ArrayList<Minion> getDeckPlayer2() {
        return deckPlayer2;
    }
    public int getPlayerTurn() {
        return playerTurn;
    }
    public int getRoundNumber() {
        return roundNumber;
    }
    public boolean getIsANewTurn() {
        return isANewTurn;
    }
    public void setPlayerTurn(final int playerTurn) {
        this.playerTurn = playerTurn;
    }
    public void setDeckPlayer1(final ArrayList<Minion> deckPlayer1) {
        this.deckPlayer1 = deckPlayer1;
    }
    public void setRoundNumber(final int roundNumber) {
        this.roundNumber = roundNumber;
    }
    public void setHandPlayer1(final ArrayList<Minion> handPlayer1) {
        this.handPlayer1 = handPlayer1;
    }
    public void setHandPlayer2(final ArrayList<Minion> handPlayer2) {
        this.handPlayer2 = handPlayer2;
    }
    public void setDeckPlayer2(final ArrayList<Minion> deckPlayer2) {
        this.deckPlayer2 = deckPlayer2;
    }
    public void setIsANewTurn(final boolean isANewTurn) {
        this.isANewTurn = isANewTurn;
    }
}


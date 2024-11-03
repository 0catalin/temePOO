package main;

import java.util.ArrayList;

public class GameInfo {
    private ArrayList<Minion> deckPlayer1;
    private ArrayList<Minion> deckPlayer2;
    private ArrayList<Minion> handPlayer1;
    private ArrayList<Minion> handPlayer2;
    private int playerTurn;
    private int roundNumber;
    private int isANewTurn;
    public GameInfo() {
    }

    void setupStartRound(Player player1, Player player2) {
        player1.setMana(player1.getMana() + receiveMana());
        player2.setMana(player2.getMana() + receiveMana());
        if(deckPlayer1.size() >= 1) {
            handPlayer1.add(deckPlayer1.get(0));
            deckPlayer1.remove(0);
        }
        if(deckPlayer2.size() >= 1) {
            handPlayer2.add(deckPlayer2.get(0));
            deckPlayer2.remove(0);
        }
    }

    String addCardToTable(Player player, TableCards tableCards, int CardIDx) {

        Minion cardToMove = null;
        if(playerTurn == 2) {
            if(handPlayer2.size() > CardIDx) {
                cardToMove = handPlayer2.get(CardIDx);
            }
            else {return "Not enough cards in hand";}
            if(player.getMana() < cardToMove.getMana()) {
                return "Not enough mana to place card on table.";
            }
            if(cardToMove.getRow().equals("back")) {
                if (tableCards.getRow(0).size() == 5) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(0).add(cardToMove);
            }
            if(cardToMove.getRow().equals("front")) {
                if (tableCards.getRow(1).size() == 5) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(1).add(cardToMove);
            }
            handPlayer2.remove(CardIDx);
            player.setMana(player.getMana() - cardToMove.getMana());
        }
        else {
            if(handPlayer1.size() > CardIDx) {
                cardToMove = handPlayer1.get(CardIDx);
            }
            else {return "Not enough cards in hand";}
            if (player.getMana() < cardToMove.getMana()) {
                return "Not enough mana to place card on table.";
            }
            if (cardToMove.getRow().equals("back")) {
                if (tableCards.getRow(3).size() == 5) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(3).add(cardToMove);
            }
            if(cardToMove.getRow().equals("front")) {
                if (tableCards.getRow(2).size() == 5) {
                    return "Cannot place card on table since row is full.";
                }
                tableCards.getRow(2).add(cardToMove);
            }
            handPlayer1.remove(CardIDx);
            player.setMana(player.getMana() - cardToMove.getMana());
        }
        return "";
    }

    int receiveMana() {
        if(roundNumber < 11) { return roundNumber;}
        return 10;
    }
    ArrayList<Minion> getDeckPlayer1() {return deckPlayer1;}
    ArrayList<Minion> getHandPlayer1() {return handPlayer1;}
    ArrayList<Minion> getHandPlayer2() {return handPlayer2;}
    ArrayList<Minion> getDeckPlayer2() {return deckPlayer2;}
    int getPlayerTurn() {return playerTurn;}
    int getRoundNumber() {return roundNumber;}
    int getIsANewTurn() {return isANewTurn;}
    void setPlayerTurn(int playerTurn) {this.playerTurn = playerTurn;}
    void setDeckPlayer1(ArrayList<Minion> deckPlayer1) {
        this.deckPlayer1 = deckPlayer1;
    }
    void setRoundNumber(int roundNumber) {this.roundNumber = roundNumber;}
    void setHandPlayer1(ArrayList<Minion> handPlayer1) {
        this.handPlayer1 = handPlayer1;
    }
    void setHandPlayer2(ArrayList<Minion> handPlayer2) { this.handPlayer2 = handPlayer2;}
    void setDeckPlayer2(ArrayList<Minion> deckPlayer2) {this.deckPlayer2 = deckPlayer2;}
    void setIsANewTurn(int isANewTurn) {this.isANewTurn = isANewTurn;}
}


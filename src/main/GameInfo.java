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
        handPlayer1.add(deckPlayer1.get(0));
        handPlayer2.add(deckPlayer2.get(0));
        deckPlayer1.remove(0);
        deckPlayer2.remove(0);
    }

    int receiveMana() {
        if(roundNumber < 11) { return roundNumber;}
        return 0;
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


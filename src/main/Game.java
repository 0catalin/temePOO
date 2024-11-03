package main;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import java.util.ArrayList;

public class Game {
    private static Game instance = null;

    static {
        instance = new Game();
    }
    TableCards tableCards = new TableCards();
    private Player player1;
    private Player player2;
    private ArrayList<GameInput> inputOfGame;
    private StartGameInput startGameInput;
    private Input initialInput;
    private GameInfo gameInfo;
    private Statistics statistics;
    private Game() {}

    public void applyParams(Input input) {
        player1 = new Player(input.getPlayerOneDecks());
        player2 = new Player(input.getPlayerTwoDecks());
        inputOfGame = input.getGames();
        startGameInput = null;
        initialInput = input;
        gameInfo = new GameInfo();
        tableCards = new TableCards();
        statistics = new Statistics();
    }

    public void loopOver(ObjectMapper objectMapper, ArrayNode output) {
        gameInfo = new GameInfo();
        for(GameInput gameinput: inputOfGame) {
            StartGameInput startInput = gameinput.getStartGame();
            player1.setHero(new Hero(startInput.getPlayerOneHero()));
            player2.setHero(new Hero(startInput.getPlayerTwoHero()));
            setStartGameInput(startInput);
            player1.getDecks().shuffleDeck(startInput.getPlayerOneDeckIdx(), startInput.getShuffleSeed());
            player2.getDecks().shuffleDeck(startInput.getPlayerTwoDeckIdx(), startInput.getShuffleSeed());
            gameInfo.setDeckPlayer1(player1.getDecks().getDecks().get(startInput.getPlayerOneDeckIdx()));
            gameInfo.setDeckPlayer2(player2.getDecks().getDecks().get(startInput.getPlayerTwoDeckIdx()));
            gameInfo.setHandPlayer1(new ArrayList<>());
            gameInfo.setHandPlayer2(new ArrayList<>());
            gameInfo.setPlayerTurn(startInput.getStartingPlayer());
            gameInfo.setRoundNumber(1); // de luat in cons cand se face endround
            gameInfo.setIsANewTurn(1);
            for(ActionsInput actions: gameinput.getActions()) {
                if (gameInfo.getIsANewTurn() == 1) {
                    gameInfo.setupStartRound(player1, player2);
                    gameInfo.setIsANewTurn(0);
                }
                if(actions.getCommand().equals("getPlayerDeck")) {
                    getPlayerDeck(objectMapper, output, actions.getPlayerIdx());
                }
                if(actions.getCommand().equals("getPlayerHero")) {
                    getPlayerHero(objectMapper, output, player1, player2, actions.getPlayerIdx());
                }
                if(actions.getCommand().equals("getPlayerTurn")) {
                    getPlayerTurn(objectMapper, output);
                }
                if(actions.getCommand().equals("endPlayerTurn")) {
                    endPlayerTurn();
                }
                if(actions.getCommand().equals("getPlayerMana")) {
                    getPlayerMana(objectMapper, output, actions.getPlayerIdx());
                }
                if(actions.getCommand().equals("getCardsInHand")) {
                    if(actions.getPlayerIdx() == 1) {
                        getCardsInHand(objectMapper, output, gameInfo.getHandPlayer1(), actions.getPlayerIdx());
                    }
                    else {getCardsInHand(objectMapper, output, gameInfo.getHandPlayer2(), actions.getPlayerIdx());}
                }
                if(actions.getCommand().equals("getCardsOnTable")) {
                    getCardsOnTable(objectMapper, output, tableCards);
                }
                if(actions.getCommand().equals("placeCard")) {
                    String outPut;
                    if(gameInfo.getPlayerTurn() == 1)
                    {outPut = gameInfo.addCardToTable(player1, tableCards, actions.getHandIdx());}
                    else {outPut = gameInfo.addCardToTable(player2, tableCards, actions.getHandIdx());}
                    if(!outPut.isEmpty()) {
                        printPlaceError(outPut, actions.getHandIdx(), output, objectMapper);
                    }
                }
                if(actions.getCommand().equals("cardUsesAttack")) {
                    String s = tableCards.cardAttack(actions.getCardAttacker(), actions.getCardAttacked());
                    if(!s.isEmpty()) {
                        cardUsesAttackError(objectMapper, output, actions.getCardAttacker(), actions.getCardAttacked(), s);
                    }
                }

                if(actions.getCommand().equals("getCardAtPosition")) {
                    getCardAtPosition(objectMapper, output, actions.getX(), actions.getY());
                }

                if(actions.getCommand().equals("cardUsesAbility")) {
                    String s1 = tableCards.cardUsesAbility(actions.getCardAttacker(), actions.getCardAttacked());
                    if(!s1.isEmpty()) {
                        cardUsesSpecialAttackError(objectMapper, output, actions.getCardAttacker(), actions.getCardAttacked(), s1);
                    }
                }
                if(actions.getCommand().equals("useAttackHero")) {
                    String s2 = tableCards.cardAttackHero(actions.getCardAttacker());
                    if(!s2.isEmpty()) {
                        cardUsesAttackHeroError(objectMapper, output, actions.getCardAttacker(), s2);
                    }
                    else{
                        if(gameInfo.getPlayerTurn() == 1) {
                            s2 = player2.getHero().attackTheHero(1, tableCards.getMinion(actions.getCardAttacker()), statistics);
                        }
                        else {
                            s2 = player1.getHero().attackTheHero(2, tableCards.getMinion(actions.getCardAttacker()), statistics);
                        }
                        if(!s2.isEmpty()) {
                            ObjectNode commandObject = objectMapper.createObjectNode();
                            commandObject.put("gameEnded", s2);
                            output.add(commandObject);
                        }
                    }
                }

                if(actions.getCommand().equals("getPlayerOneWins")) {
                    ObjectNode commandObject = objectMapper.createObjectNode();
                    commandObject.put("command", "getPlayerOneWins");
                    commandObject.put("output", statistics.getPlayer1Wins());
                    output.add(commandObject);
                }

                if(actions.getCommand().equals("getPlayerTwoWins")) {
                    ObjectNode commandObject = objectMapper.createObjectNode();
                    commandObject.put("command", "getPlayerTwoWins");
                    commandObject.put("output", statistics.getPlayer2Wins());
                    output.add(commandObject);
                }

                if(actions.getCommand().equals("getTotalGamesPlayed")) {
                    ObjectNode commandObject = objectMapper.createObjectNode();
                    commandObject.put("command", "getTotalGamesPlayed");
                    commandObject.put("output", statistics.getTotalGamesPlayed());
                    output.add(commandObject);
                }

            }
            player1 = new Player(initialInput.getPlayerOneDecks());
            player2 = new Player(initialInput.getPlayerTwoDecks());
        }
    }




    void getPlayerDeck(ObjectMapper objectMapper, ArrayNode output, int playerIDx) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "getPlayerDeck");
        commandObject.put("playerIdx", playerIDx);
        ArrayList<Minion> deck;
        if(playerIDx == 1) {
            deck = gameInfo.getDeckPlayer1(); }
        else {
            deck = gameInfo.getDeckPlayer2();
        }
        ArrayNode outputCorrespondent = objectMapper.createArrayNode();
        for(Minion card: deck) {
            card.printMinion(outputCorrespondent, objectMapper);
        }
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    void getPlayerHero(ObjectMapper objectMapper, ArrayNode output, Player player1, Player player2, int IDX) {
        Hero hero;
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "getPlayerHero");
        commandObject.put("playerIdx", IDX);
        if(IDX == 1) {
            hero = player1.getHero();
        }
        else {
            hero = player2.getHero();
        }
        ObjectNode outputCorrespondent = objectMapper.createObjectNode();
        outputCorrespondent.put("mana", hero.getMana());
        outputCorrespondent.put("description", hero.getDescription());
        ArrayNode colors = objectMapper.createArrayNode();
        for(String s: hero.getColors()) {colors.add(s);}
        outputCorrespondent.set("colors", colors);
        outputCorrespondent.put("name", hero.getName());
        outputCorrespondent.put("health", hero.getHealth());
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    void getPlayerTurn(ObjectMapper objectMapper, ArrayNode output) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "getPlayerTurn");
        commandObject.put("output", gameInfo.getPlayerTurn());
        output.add(commandObject);
    }

    void endPlayerTurn() {
        if(gameInfo.getPlayerTurn() == 2) { tableCards.clearCards(2);
            gameInfo.setPlayerTurn(1); }
        else { tableCards.clearCards(1);
            gameInfo.setPlayerTurn(2);}
        if(gameInfo.getPlayerTurn() == startGameInput.getStartingPlayer()) {
            gameInfo.setRoundNumber(gameInfo.getRoundNumber() + 1);
            gameInfo.setIsANewTurn(1);
        }
    }

    void getPlayerMana(ObjectMapper objectMapper, ArrayNode output, int IDX) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "getPlayerMana");
        commandObject.put("playerIdx", IDX);
        if(IDX == 1) { commandObject.put("output", player1.getMana()); }
        else {commandObject.put("output", player2.getMana());}
        output.add(commandObject);
    }

    private void getCardsInHand(ObjectMapper objectMapper, ArrayNode output, ArrayList<Minion> handPlayer, int IDX) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "getCardsInHand");
        commandObject.put("playerIdx", IDX);
        ArrayNode outputCorrespondent = objectMapper.createArrayNode();
        for(Minion card: handPlayer) {
            card.printMinion(outputCorrespondent, objectMapper);
        }
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    private void getCardsOnTable(ObjectMapper objectMapper, ArrayNode output, TableCards tableCards) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "getCardsOnTable");
        ArrayNode outputCorrespondent = objectMapper.createArrayNode();
        tableCards.printTable(objectMapper, outputCorrespondent);
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    private void printPlaceError(String errorMessage, int handIDX, ArrayNode output, ObjectMapper objectMapper) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "placeCard");
        commandObject.put("handIdx", handIDX);
        commandObject.put("error", errorMessage);
        output.add(commandObject);
    }

    private void cardUsesAttackError(ObjectMapper objectMapper, ArrayNode output, Coordinates cardAttacker, Coordinates cardAttacked, String s) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "cardUsesAttack");
        ObjectNode attackNode = objectMapper.createObjectNode();
        ObjectNode attackedNode = objectMapper.createObjectNode();
        attackedNode.put("x", cardAttacked.getX());
        attackedNode.put("y", cardAttacked.getY());
        attackNode.put("x", cardAttacker.getX());
        attackNode.put("y", cardAttacker.getY());
        commandObject.set("cardAttacker", attackNode);
        commandObject.set("cardAttacked", attackedNode);
        commandObject.put("error", s);
        output.add(commandObject);
    }
    private void cardUsesSpecialAttackError(ObjectMapper objectMapper, ArrayNode output, Coordinates cardAttacker, Coordinates cardAttacked, String s) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "cardUsesAbility");
        ObjectNode attackNode = objectMapper.createObjectNode();
        ObjectNode attackedNode = objectMapper.createObjectNode();
        attackedNode.put("x", cardAttacked.getX());
        attackedNode.put("y", cardAttacked.getY());
        attackNode.put("x", cardAttacker.getX());
        attackNode.put("y", cardAttacker.getY());
        commandObject.set("cardAttacker", attackNode);
        commandObject.set("cardAttacked", attackedNode);
        commandObject.put("error", s);
        output.add(commandObject);
    }

    private void cardUsesAttackHeroError(ObjectMapper objectMapper, ArrayNode output, Coordinates cardAttacker, String s) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "useAttackHero");
        ObjectNode attackNode = objectMapper.createObjectNode();
        attackNode.put("x", cardAttacker.getX());
        attackNode.put("y", cardAttacker.getY());
        commandObject.set("cardAttacker", attackNode);
        commandObject.put("error", s);
        output.add(commandObject);
    }

    private void getCardAtPosition(ObjectMapper objectMapper, ArrayNode output, int x, int y) {
        ObjectNode commandObject = objectMapper.createObjectNode();
        commandObject.put("command", "getCardAtPosition");
        commandObject.put("x", x);
        commandObject.put("y", y);
        if(tableCards.getRow(x).size() < y) {
            commandObject.put("output", "No card available at that position.");
        }
        else {
            ObjectNode cardNode = objectMapper.createObjectNode();
            tableCards.getRow(x).get(y).addMinionToObjectNode(cardNode, objectMapper);
            commandObject.set("output", cardNode);
        }
        output.add(commandObject);
    }

    public static Game getInstance() {return instance;}

    Player getPlayer1() {return player1;}
    Player getPlayer2() {return player2;}
    StartGameInput getStartGameInput() {return startGameInput;}
    void setStartGameInput(StartGameInput startGameInput) {this.startGameInput = startGameInput;}
    void setInitialInput(Input initialInput) {this.initialInput = initialInput;}
    Input getInitialInput() {return initialInput;}
}


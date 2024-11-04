package main;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import fileio.Coordinates;


import java.util.ArrayList;

public final class Game {
    private static Game instance = null;

    static {
        instance = new Game();
    }
    private TableCards tableCards = new TableCards();
    private Player player1;
    private Player player2;
    private ArrayList<GameInput> inputOfGame;
    private StartGameInput startGameInput;
    private Input initialInput;
    private GameInfo gameInfo;
    private Statistics statistics;
    private Game() { }

    public void applyParams(final Input input) {
        player1 = new Player(input.getPlayerOneDecks());
        player2 = new Player(input.getPlayerTwoDecks());
        inputOfGame = input.getGames();
        startGameInput = null;
        initialInput = input;
        gameInfo = new GameInfo();
        tableCards = new TableCards();
        statistics = new Statistics();
    }

    public void loopOver(final ObjectMapper mapper, final ArrayNode output) {
        gameInfo = new GameInfo();
        for (GameInput gameinput: inputOfGame) {
            StartGameInput startInput = gameinput.getStartGame();
            player1.setHero(new Hero(startInput.getPlayerOneHero()));
            player1.setMana(0);
            player2.setHero(new Hero(startInput.getPlayerTwoHero()));
            player2.setMana(0);
            tableCards = new TableCards();
            setStartGameInput(startInput);
            int deckIdx1 = startInput.getPlayerOneDeckIdx();
            int deckIdx2 = startInput.getPlayerTwoDeckIdx();
            player1.getDecks().shuffleDeck(deckIdx1, startInput.getShuffleSeed());
            player2.getDecks().shuffleDeck(deckIdx2, startInput.getShuffleSeed());
            gameInfo.setDeckPlayer1(player1.getDecks().getDecks().get(deckIdx1));
            gameInfo.setDeckPlayer2(player2.getDecks().getDecks().get(deckIdx2));
            gameInfo.setHandPlayer1(new ArrayList<>());
            gameInfo.setHandPlayer2(new ArrayList<>());
            gameInfo.setPlayerTurn(startInput.getStartingPlayer());
            gameInfo.setRoundNumber(1);
            gameInfo.setIsANewTurn(true);
            for (ActionsInput actions: gameinput.getActions()) {
                if (gameInfo.getIsANewTurn()) {
                    gameInfo.setupStartRound(player1, player2);
                    gameInfo.setIsANewTurn(false);
                }
                if (actions.getCommand().equals("getPlayerDeck")) {
                    getPlayerDeck(mapper, output, actions.getPlayerIdx());
                }
                if (actions.getCommand().equals("getPlayerHero")) {
                    getPlayerHero(mapper, output, actions.getPlayerIdx());
                }
                if (actions.getCommand().equals("getPlayerTurn")) {
                    getPlayerTurn(mapper, output);
                }
                if (actions.getCommand().equals("endPlayerTurn")) {
                    endPlayerTurn();
                }
                if (actions.getCommand().equals("getPlayerMana")) {
                    getPlayerMana(mapper, output, actions.getPlayerIdx());
                }
                if (actions.getCommand().equals("getCardsInHand")) {
                    if (actions.getPlayerIdx() == 1) {
                        ArrayList<Minion> handPlayer1 = gameInfo.getHandPlayer1();
                        getCardsInHand(mapper, output, handPlayer1, actions.getPlayerIdx());
                    } else {
                        ArrayList<Minion> handPlayer2 = gameInfo.getHandPlayer2();
                        getCardsInHand(mapper, output, handPlayer2, actions.getPlayerIdx());
                    }
                }
                if (actions.getCommand().equals("getCardsOnTable")) {
                    getCardsOnTable(mapper, output);
                }
                if (actions.getCommand().equals("placeCard")) {
                    String outPut;
                    if (gameInfo.getPlayerTurn() == 1) {
                        outPut = gameInfo.addCardToTable(player1, tableCards, actions.getHandIdx());
                    } else {
                        outPut = gameInfo.addCardToTable(player2, tableCards, actions.getHandIdx());
                    }
                    if (!outPut.isEmpty()) {
                        printPlaceError(outPut, actions.getHandIdx(), output, mapper);
                    }
                }
                if (actions.getCommand().equals("cardUsesAttack")) {
                    Coordinates attacker = actions.getCardAttacker();
                    Coordinates attacked = actions.getCardAttacked();
                    String error = tableCards.cardAttack(attacker, attacked);
                    if (!error.isEmpty()) {
                        cardUsesAttackError(mapper, output, attacker, attacked, error);
                    }
                }

                if (actions.getCommand().equals("getCardAtPosition")) {
                    getCardAtPosition(mapper, output, actions.getX(), actions.getY());
                }
                if (actions.getCommand().equals("cardUsesAbility")) {
                    Coordinates attacker = actions.getCardAttacker();
                    Coordinates attacked = actions.getCardAttacked();
                    String error = tableCards.cardUsesAbility(attacker, attacked);
                    if (!error.isEmpty()) {
                        cardUsesSpecialAttackError(mapper, output, attacker, attacked, error);
                    }
                }
                if (actions.getCommand().equals("useAttackHero")) {
                    Coordinates attacker = actions.getCardAttacker();
                    String error = tableCards.cardAttackHero(attacker);
                    if (!error.isEmpty()) {
                        cardUsesAttackHeroError(mapper, output, attacker, error);
                    } else {
                        Minion atacker = tableCards.getMinion(actions.getCardAttacker());
                        if (gameInfo.getPlayerTurn() == 1) {
                            error = player2.getHero().attackTheHero(1, atacker, statistics);
                        } else {
                            error = player1.getHero().attackTheHero(2, atacker, statistics);
                        }
                        if (!error.isEmpty()) {
                            ObjectNode commandObject = mapper.createObjectNode();
                            commandObject.put("gameEnded", error);
                            output.add(commandObject);
                        }
                    }
                }
                if (actions.getCommand().equals("getPlayerOneWins")) {
                    ObjectNode commandObject = mapper.createObjectNode();
                    commandObject.put("command", "getPlayerOneWins");
                    commandObject.put("output", statistics.getPlayer1Wins());
                    output.add(commandObject);
                }
                if (actions.getCommand().equals("getPlayerTwoWins")) {
                    ObjectNode commandObject = mapper.createObjectNode();
                    commandObject.put("command", "getPlayerTwoWins");
                    commandObject.put("output", statistics.getPlayer2Wins());
                    output.add(commandObject);
                }
                if (actions.getCommand().equals("getTotalGamesPlayed")) {
                    ObjectNode commandObject = mapper.createObjectNode();
                    commandObject.put("command", "getTotalGamesPlayed");
                    commandObject.put("output", statistics.getTotalGamesPlayed());
                    output.add(commandObject);
                }
                if (actions.getCommand().equals("getFrozenCardsOnTable")) {
                    ObjectNode commandObject = mapper.createObjectNode();
                    commandObject.put("command", "getFrozenCardsOnTable");
                    ArrayNode outputCorrespondent = tableCards.printFrozenTable(mapper);
                    commandObject.set("output", outputCorrespondent);
                    output.add(commandObject);
                }
                if (actions.getCommand().equals("useHeroAbility")) {
                    String error;
                    if (gameInfo.getPlayerTurn() == 1) {
                        error = tableCards.heroAbility(player1, 1, actions.getAffectedRow());
                    } else {
                        error = tableCards.heroAbility(player2, 2, actions.getAffectedRow());
                    }
                    if (!error.isEmpty()) {
                        ObjectNode commandObject = mapper.createObjectNode();
                        commandObject.put("command", "useHeroAbility");
                        commandObject.put("affectedRow", actions.getAffectedRow());
                        commandObject.put("error", error);
                        output.add(commandObject);
                    }
                }
            }
            player1 = new Player(initialInput.getPlayerOneDecks());
            player2 = new Player(initialInput.getPlayerTwoDecks());
        }
    }




    private void getPlayerDeck(final ObjectMapper mapper, final ArrayNode output, final int idx) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getPlayerDeck");
        commandObject.put("playerIdx", idx);
        ArrayList<Minion> deck;
        if (idx == 1) {
            deck = gameInfo.getDeckPlayer1();
        } else {
            deck = gameInfo.getDeckPlayer2();
        }
        ArrayNode outputCorrespondent = mapper.createArrayNode();
        for (Minion card: deck) {
            card.printMinion(outputCorrespondent, mapper);
        }
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    private void getPlayerHero(final ObjectMapper mapper, final ArrayNode output, final int idx) {
        Hero hero;
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getPlayerHero");
        commandObject.put("playerIdx", idx);
        if (idx == 1) {
            hero = player1.getHero();
        } else {
            hero = player2.getHero();
        }
        ObjectNode outputCorrespondent = mapper.createObjectNode();
        outputCorrespondent.put("mana", hero.getMana());
        outputCorrespondent.put("description", hero.getDescription());
        ArrayNode colors = mapper.createArrayNode();
        for (String s: hero.getColors()) {
            colors.add(s);
        }
        outputCorrespondent.set("colors", colors);
        outputCorrespondent.put("name", hero.getName());
        outputCorrespondent.put("health", hero.getHealth());
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    private void getPlayerTurn(final ObjectMapper mapper, final ArrayNode output) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getPlayerTurn");
        commandObject.put("output", gameInfo.getPlayerTurn());
        output.add(commandObject);
    }

    private void endPlayerTurn() {
        if (gameInfo.getPlayerTurn() == 2) {
            tableCards.clearCards(2);
            player2.getHero().setHasAttacked(false);
            gameInfo.setPlayerTurn(1);
        } else {
            tableCards.clearCards(1);
            player1.getHero().setHasAttacked(false);
            gameInfo.setPlayerTurn(2);
        }
        if (gameInfo.getPlayerTurn() == startGameInput.getStartingPlayer()) {
            gameInfo.setRoundNumber(gameInfo.getRoundNumber() + 1);
            gameInfo.setIsANewTurn(true);
        }
    }

    private void getPlayerMana(final ObjectMapper mapper, final ArrayNode output, final int idx) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getPlayerMana");
        commandObject.put("playerIdx", idx);
        if (idx == 1) {
            commandObject.put("output", player1.getMana());
        } else {
            commandObject.put("output", player2.getMana());
        }
        output.add(commandObject);
    }

    private void getCardsInHand(final ObjectMapper mapper, final ArrayNode output, final ArrayList<Minion> handPlayer, final int idx) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getCardsInHand");
        commandObject.put("playerIdx", idx);
        ArrayNode outputCorrespondent = mapper.createArrayNode();
        for (Minion card: handPlayer) {
            card.printMinion(outputCorrespondent, mapper);
        }
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    private void getCardsOnTable(final ObjectMapper mapper, final ArrayNode output) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getCardsOnTable");
        ArrayNode outputCorrespondent = mapper.createArrayNode();
        tableCards.printTable(mapper, outputCorrespondent);
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }

    private void printPlaceError(final String errorMessage, final int handIDX, final ArrayNode output, final ObjectMapper mapper) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "placeCard");
        commandObject.put("handIdx", handIDX);
        commandObject.put("error", errorMessage);
        output.add(commandObject);
    }

    private void cardUsesAttackError(final ObjectMapper mapper, final ArrayNode output, final Coordinates cardAttacker, final Coordinates cardAttacked, final String error) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "cardUsesAttack");
        ObjectNode attackNode = mapper.createObjectNode();
        ObjectNode attackedNode = mapper.createObjectNode();
        attackedNode.put("x", cardAttacked.getX());
        attackedNode.put("y", cardAttacked.getY());
        attackNode.put("x", cardAttacker.getX());
        attackNode.put("y", cardAttacker.getY());
        commandObject.set("cardAttacker", attackNode);
        commandObject.set("cardAttacked", attackedNode);
        commandObject.put("error", error);
        output.add(commandObject);
    }
    private void cardUsesSpecialAttackError(final ObjectMapper mapper, final ArrayNode output, final Coordinates cardAttacker, final Coordinates cardAttacked, final String error) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "cardUsesAbility");
        ObjectNode attackNode = mapper.createObjectNode();
        ObjectNode attackedNode = mapper.createObjectNode();
        attackedNode.put("x", cardAttacked.getX());
        attackedNode.put("y", cardAttacked.getY());
        attackNode.put("x", cardAttacker.getX());
        attackNode.put("y", cardAttacker.getY());
        commandObject.set("cardAttacker", attackNode);
        commandObject.set("cardAttacked", attackedNode);
        commandObject.put("error", error);
        output.add(commandObject);
    }

    private void cardUsesAttackHeroError(final ObjectMapper mapper, final ArrayNode output, final Coordinates cardAttacker, final String error) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "useAttackHero");
        ObjectNode attackNode = mapper.createObjectNode();
        attackNode.put("x", cardAttacker.getX());
        attackNode.put("y", cardAttacker.getY());
        commandObject.set("cardAttacker", attackNode);
        commandObject.put("error", error);
        output.add(commandObject);
    }

    private void getCardAtPosition(final ObjectMapper mapper, final ArrayNode output, final int x, final int y) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getCardAtPosition");
        commandObject.put("x", x);
        commandObject.put("y", y);
        if (tableCards.getRow(x).size() < y) {
            commandObject.put("output", "No card available at that position.");
        } else {
            ObjectNode cardNode = mapper.createObjectNode();
            tableCards.getRow(x).get(y).addMinionToObjectNode(cardNode, mapper);
            commandObject.set("output", cardNode);
        }
        output.add(commandObject);
    }

    public static Game getInstance() {
        return instance;
    }

    Player getPlayer1() {
        return player1;
    }
    Player getPlayer2() {
        return player2;
    }
    StartGameInput getStartGameInput() {
        return startGameInput;
    }
    private void setStartGameInput(final StartGameInput startGameInput) {
        this.startGameInput = startGameInput;
    }
    void setInitialInput(final Input initialInput) {
        this.initialInput = initialInput;
    }
    Input getInitialInput() {
        return initialInput;
    }
}


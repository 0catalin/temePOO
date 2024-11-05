package game;
import characters.Hero;
import characters.Minion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import fileio.Coordinates;
import players.Player;

import java.util.ArrayList;

/**
 * the Game singleton, stores and does everything needed for the game to work
 * it also contains the function looping through the commands
 */
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

    /**
     * applies parameters, it is a Singleton specific constructor
     * @param input the input used to construct it
     */
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

    /**
     * Loops through all the commands and assures that the game works properly
     * @param mapper the ObjectMapper object
     * @param output contains the arrayNode printed in the .out files
     */
    public void loopOver(final ObjectMapper mapper, final ArrayNode output) {
        gameInfo = new GameInfo();
        for (GameInput gameinput: inputOfGame) {  // iterates over the game inputs
            StartGameInput startInput = gameinput.getStartGame(); // initializes startInput
            player1.setHero(new Hero(startInput.getPlayerOneHero()));
            player2.setHero(new Hero(startInput.getPlayerTwoHero()));
            tableCards = new TableCards();
            setStartGameInput(startInput);
            int deckIdx1 = startInput.getPlayerOneDeckIdx();
            int deckIdx2 = startInput.getPlayerTwoDeckIdx(); // sets the chosen player decks
            player1.getDecks().shuffleDeck(deckIdx1, startInput.getShuffleSeed());
            player2.getDecks().shuffleDeck(deckIdx2, startInput.getShuffleSeed());
            gameInfo.setDeckPlayer1(player1.getDecks().getDecks().get(deckIdx1));
            gameInfo.setDeckPlayer2(player2.getDecks().getDecks().get(deckIdx2));
            gameInfo.setHandPlayer1(new ArrayList<Minion>()); // sets new player hands
            gameInfo.setHandPlayer2(new ArrayList<Minion>());
            gameInfo.setPlayerTurn(startInput.getStartingPlayer());
            gameInfo.setRoundNumber(1);
            gameInfo.setIsANewTurn(true);
            for (ActionsInput actions: gameinput.getActions()) { // iterates over actions
                if (gameInfo.getIsANewTurn()) { // sets up the start of a round
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
                        cardUsesAttackError(mapper, output, attacker, attacked,
                                error, "cardUsesAttack");
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
                        cardUsesAttackError(mapper, output, attacker, attacked,
                                error, "cardUsesAbility");
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
                        heroUsesAbilityError(mapper, output, error, actions.getAffectedRow());
                    }
                }
            }
            player1 = new Player(initialInput.getPlayerOneDecks());
            player2 = new Player(initialInput.getPlayerTwoDecks());
        }
    }

    /*
        adds the player deck into the ArrayNode which will be outputted
        at the end of the main function
    */
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
    /*
        adds the player hero into the ArrayNode which will be outputted
        at the end of the main function
    */
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
    /*
        adds the current player turn into the ArrayNode which will be outputted
        at the end of the main function
    */
    private void getPlayerTurn(final ObjectMapper mapper, final ArrayNode output) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getPlayerTurn");
        commandObject.put("output", gameInfo.getPlayerTurn());
        output.add(commandObject);
    }
    /*
        Ends a player's turn by changing the current player turn and setting the
        hasAttacked and frozen fields to false
        Checks whether a new round will start and if so changes the round number and sets
        the isANewTurn variable accordingly
    */
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
    /*
        adds the player mana into the ArrayNode which will be outputted
        at the end of the main function
    */
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
    /*
        adds the cards in hand of the current player into the ArrayNode
        which will be outputted at the end of the main function
    */
    private void getCardsInHand(final ObjectMapper mapper, final ArrayNode output,
                                final ArrayList<Minion> handPlayer, final int idx) {
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
    /*
        adds the cards on table into the ArrayNode which will be outputted
        at the end of the main function
    */
    private void getCardsOnTable(final ObjectMapper mapper, final ArrayNode output) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "getCardsOnTable");
        ArrayNode outputCorrespondent = mapper.createArrayNode();
        tableCards.printTable(mapper, outputCorrespondent);
        commandObject.set("output", outputCorrespondent);
        output.add(commandObject);
    }
    /*
        adds the card placing error into the ArrayNode which will be outputted
        at the end of the main function
    */
    private void printPlaceError(final String errorMessage, final int handIDX,
                                 final ArrayNode output, final ObjectMapper mapper) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "placeCard");
        commandObject.put("handIdx", handIDX);
        commandObject.put("error", errorMessage);
        output.add(commandObject);
    }
    /*
        adds the card using attack error into the ArrayNode which will be outputted
        at the end of the main function
    */
    private void cardUsesAttackError(final ObjectMapper mapper, final ArrayNode output,
                                     final Coordinates cardAttacker, final Coordinates cardAttacked,
                                     final String error, final String command) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", command);
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
    /*
        adds the card using attack on hero error into the ArrayNode which will be outputted
        at the end of the main function
    */
    private void cardUsesAttackHeroError(final ObjectMapper mapper, final ArrayNode output,
                                         final Coordinates cardAttacker, final String error) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "useAttackHero");
        ObjectNode attackNode = mapper.createObjectNode();
        attackNode.put("x", cardAttacker.getX());
        attackNode.put("y", cardAttacker.getY());
        commandObject.set("cardAttacker", attackNode);
        commandObject.put("error", error);
        output.add(commandObject);
    }
    /*
        adds the hero using ability error into the ArrayNode which will be outputted
        at the end of the main function
    */
    private void heroUsesAbilityError(final ObjectMapper mapper,
                                      final ArrayNode output, final String error,
                                      final int affectedRow) {
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "useHeroAbility");
        commandObject.put("affectedRow", affectedRow);
        commandObject.put("error", error);
        output.add(commandObject);
    }
    /*
        adds the card at a certain position given in input into the
        ArrayNode which will be outputted at the end of the main function
    */
    private void getCardAtPosition(final ObjectMapper mapper,
                                   final ArrayNode output, final int x, final int y) {
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

    public void setStartGameInput(final StartGameInput startGameInput) {
        this.startGameInput = startGameInput;
    }

}


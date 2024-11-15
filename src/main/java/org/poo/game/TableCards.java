package org.poo.game;
import org.poo.characters.Hero;
import org.poo.characters.Minion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.Coordinates;
import org.poo.players.Player;

import java.util.ArrayList;

/**
 * class designed to store only the cards on the table
 */
public final class TableCards {
    private ArrayList<Minion>[] cardRows;
    private static final int NUMBER_OF_ARRAYS = 4;
    private static final int LAST_ROW = 3;
    public TableCards() {
        cardRows = new ArrayList[NUMBER_OF_ARRAYS];
        for (int i = 0; i < cardRows.length; i++) {
            cardRows[i] = new ArrayList<Minion>();
        }
    }

    /**
     * @param idx the index of the row
     * @return the row corresponding to the index
     */
    public ArrayList<Minion> getRow(final int idx) {
            return cardRows[idx];
    }

    /**
     * creates an arraynode for each card and adds them into the given ArrayNode
     * @param mapper the ObjectMapper
     * @param outputCorrespondent the given ArrayNode
     */
    public void printTable(final ObjectMapper mapper, final ArrayNode outputCorrespondent) {
        for (int i = 0; i < cardRows.length; i++) {
            ArrayNode separateRow = mapper.createArrayNode();
            ArrayList<Minion> row = cardRows[i];
            for (Minion minion: row) {
                minion.printMinion(separateRow, mapper);
            }
            outputCorrespondent.add(separateRow);
        }
    }

    /**
     * adds all the frozen table cards to an ArrayNode and returns it
     * @param mapper the ObjectMapper
     * @return ArrayNode with all the frozen cards
     */
    public ArrayNode printFrozenTable(final ObjectMapper mapper) {
        ArrayNode output = mapper.createArrayNode();
        for (int i = 0; i < cardRows.length; i++) {
            ArrayList<Minion> row = cardRows[i];
            for (Minion minion: row) {
                if (minion.getIsFrozen()) {
                    minion.printMinion(output, mapper);
                }
            }
        }
        return output;
    }

    /**
     * simulates an attack of a card on another card
     * @param attacker the coordinates of the attacker
     * @param attacked the coordinates of the attacked
     * @return an empty string on success and an error on failure
     */
    public String cardAttack(final Coordinates attacker, final Coordinates attacked) {
        Minion atacker = getMinion(attacker);
        Minion atacked = getMinion(attacked);
        if (!areDifferentTeams(attacker.getX(), attacked.getX())) {
            return "Attacked card does not belong to the enemy.";
        }
        if (atacker.getHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }
        if (atacker.getIsFrozen()) {
            return "Attacker card is frozen.";
        }
        if (checkOpponentTank(attacker) && (!getMinion(attacked).isTank())) {
            return "Attacked card is not of type 'Tank'.";
        }
        atacked.setHealth(atacked.getHealth() - atacker.getAttackDamage());
        if (atacked.getHealth() <= 0) {
            cardRows[attacked.getX()].remove(attacked.getY());
        }
        atacker.setHasAttacked(true);
        return "";
    }

    /**
     * simulates an ability of a card on another card
     * @param attacker the coordinates of the attacker
     * @param attacked the coordinates of the attacked
     * @return an empty string on success and an error on failure
     */
    public String cardUsesAbility(final Coordinates attacker, final Coordinates attacked) {
        Minion atacker = getMinion(attacker);
        Minion atacked = getMinion(attacked);
        if (atacker.getIsFrozen()) {
            return "Attacker card is Frozen.";
        }
        if (atacker.getHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }
        String name = atacker.getName();
        if (name.equals("Disciple") && areDifferentTeams(attacker.getX(), attacked.getX())) {
            return "Attacked card does not belong to the current player.";
        }
        if ((name.equals("The Ripper") || name.equals("Miraj") || name.equals("The Cursed One"))) {
            if ((!areDifferentTeams(attacker.getX(), attacked.getX()))) {
                return "Attacked card does not belong to the enemy.";
            }
            if (checkOpponentTank(attacker) && (!getMinion(attacked).isTank())) {
                return "Attacked card is not of type 'Tank'.";
            }
        }
        atacker.specialAttack(atacked);
        if (atacked.getHealth() <= 0) {
            cardRows[attacked.getX()].remove(attacked.getY());
        }
        atacker.setHasAttacked(true);
        return "";
    }

    /**
     * simulates an attack of a card on the hero
     * @param attacker the coordinates of the card attacker
     * @return an empty string on success and an error on failure
     */
    public String cardAttackHero(final Coordinates attacker) {
        Minion atacker = getMinion(attacker);
        if (atacker.getIsFrozen()) {
            return "Attacker card is frozen.";
        }
        if (atacker.getHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }
        if (checkOpponentTank(attacker)) {
            return "Attacked card is not of type 'Tank'.";
        }
        return "";
    }

    /**
     * simulates the hero ability on a given row
     * @param player the current player
     * @param playerIdx the player index
     * @param affectedRow the index of the attacked row
     * @return an empty string on success and an error on failure
     */
    public String heroAbility(final Player player, final int playerIdx, final int affectedRow) {
        Hero hero = player.getHero();
        String name = hero.getName();
        if (player.getMana() < hero.getMana()) {
            return "Not enough mana to use hero's ability.";
        }
        if (hero.getHasAttacked()) {
            return "Hero has already attacked this turn.";
        }
        if (name.equals("Lord Royce") || name.equals("Empress Thorina")) {
            if (isRowSameTeam(playerIdx, affectedRow)) {
                return "Selected row does not belong to the enemy.";
            }
        }
        if (name.equals("General Kocioraw") || name.equals("King Mudface")) {
            if (!isRowSameTeam(playerIdx, affectedRow)) {
                return "Selected row does not belong to the current player.";
            }
        }
        if (name.equals("Lord Royce")) {
            subZero(affectedRow);
        }
        if (name.equals("Empress Thorina")) {
           lowBlow(affectedRow);
        }
        if (name.equals("General Kocioraw")) {
            bloodThirst(affectedRow);
        }
        if (name.equals("King Mudface")) {
            earthBorn(affectedRow);
        }
        player.setMana(player.getMana() - hero.getMana());
        hero.setHasAttacked(true);
        return "";
    }

    /**
     * clears all the frozen and sets the hasAttacked
     * flag to false when a player's round ends
     * @param player the player id
     */
    public void clearCards(final int player) {
        if (player == 2) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < cardRows[i].size(); j++) {
                    cardRows[i].get(j).setIsFrozen(false);
                    cardRows[i].get(j).setHasAttacked(false);
                }
            }
        } else {
            for (int i = 2; i < NUMBER_OF_ARRAYS; i++) {
                for (int j = 0; j < cardRows[i].size(); j++) {
                    cardRows[i].get(j).setIsFrozen(false);
                    cardRows[i].get(j).setHasAttacked(false);
                }
            }
        }
    }

    /**
     * gets minion based on its coordinates
     * @param coordinates the coordinates of the minion
     * @return the Minion situated at the given table coordinates
     */
    public Minion getMinion(final Coordinates coordinates) {
        return getRow(coordinates.getX()).get(coordinates.getY());
    }
    /*
    checks whether the opponent has any tank in the lines and returns true if found
    */
    private boolean checkOpponentTank(final Coordinates coordinates) {
        if (coordinates.getX() == 0 || coordinates.getX() == 1) {
            for (int i = 0; i < getRow(2).size(); i++) {
                if (getRow(2).get(i).isTank()) {
                    return true;
                }
            }
            return false;
        } else {
            for (int i = 0; i < getRow(1).size(); i++) {
                if (getRow(1).get(i).isTank()) {
                    return true;
                }
            }
            return false;
        }
    }
    /*
    implements subZero attack, iterates over a row and freezes everything
    */
    private void subZero(final int affectedRow) {
        ArrayList<Minion> row = getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setIsFrozen(true);
        }
    }
    /*
    implements lowBlow attack, eliminates the minion with the maximum health
    */
    private void lowBlow(final int affectedRow) {
        ArrayList<Minion> row = getRow(affectedRow);
        Minion maxHealthMinion = new Minion();
        int maxHealthIndex = 0;
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i).getHealth() > maxHealthMinion.getHealth()) {
                maxHealthMinion = row.get(i);
                maxHealthIndex = i;
            }
        }
        row.remove(maxHealthIndex);
    }
    /*
    implements EarthBorn attack, iterates over a row and increases health
    */
    private void earthBorn(final int affectedRow) {
        ArrayList<Minion> row = getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setHealth(row.get(i).getHealth() + 1);
        }
    }
    /*
    implements BloodThirst attack, iterates over a row and increases attackDamage
    */
    private void bloodThirst(final int affectedRow) {
        ArrayList<Minion> row = getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setAttackDamage(row.get(i).getAttackDamage() + 1);
        }
    }
    /*
    checks whether 2 cards from 2 rows, x1 and x2, are in different teams
    */
    private boolean areDifferentTeams(final int x1, final int x2) {
        boolean team1 = (x1 == 0 || x1 == 1) && (x2 == 0 || x2 == 1);
        boolean team2 = (x1 == 2 || x1 == LAST_ROW) && (x2 == 2 || x2 == LAST_ROW);
        return !(team1 || team2);
    }
    /*
    checks whether a player and a row are in same team
     */
    private boolean isRowSameTeam(final int player, final int row) {
        boolean team1 = (player == 1 && row == 2) || (player == 1 && row == LAST_ROW);
        boolean team2 = (player == 2 && row == 0) || (player == 2 && row == 1);
        return (team1 || team2);
    }

}

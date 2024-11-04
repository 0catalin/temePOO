package main;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;
import java.util.ArrayList;

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

    public ArrayList<Minion> getRow(final int idx) {
            return cardRows[idx];
    }

    public void setRow(final int idx, final ArrayList<Minion> row) {
        cardRows[idx] = row;
    }

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

    public String heroAbility(final Player player, final int playerIDX, final int affectedRow) {
        Hero hero = player.getHero();
        String name = hero.getName();
        if (player.getMana() < hero.getMana()) {
            return "Not enough mana to use hero's ability.";
        }
        if (hero.getHasAttacked()) {
            return "Hero has already attacked this turn.";
        }
        if (name.equals("Lord Royce") || name.equals("Empress Thorina")) {
            if (isRowSameTeam(playerIDX, affectedRow)) {
                return "Selected row does not belong to the enemy.";
            }
        }
        if (name.equals("General Kocioraw") || name.equals("King Mudface")) {
            if (!isRowSameTeam(playerIDX, affectedRow)) {
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

    public Minion getMinion(final Coordinates coordinates) {
        return getRow(coordinates.getX()).get(coordinates.getY());
    }
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

    private void subZero(final int affectedRow) {
        ArrayList<Minion> row = getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setIsFrozen(true);
        }
    }

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

    private void earthBorn(final int affectedRow) {
        ArrayList<Minion> row = getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setHealth(row.get(i).getHealth() + 1);
        }
    }

    private void bloodThirst(final int affectedRow) {
        ArrayList<Minion> row = getRow(affectedRow);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setAttackDamage(row.get(i).getAttackDamage() + 1);
        }
    }

    private boolean areDifferentTeams(final int x1, final int x2) {
        boolean team1 = (x1 == 0 || x1 == 1) && (x2 == 0 || x2 == 1);
        boolean team2 = (x1 == 2 || x1 == LAST_ROW) && (x2 == 2 || x2 == LAST_ROW);
        return !(team1 || team2);
    }

    private boolean isRowSameTeam(final int player, final int row) {
        boolean team1 = (player == 1 && row == 2) || (player == 1 && row == LAST_ROW);
        boolean team2 = (player == 2 && row == 0) || (player == 2 && row == 1);
        return (team1 || team2);
    }
}

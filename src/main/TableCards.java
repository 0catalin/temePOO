package main;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;

import java.util.ArrayList;

public class TableCards {
    private ArrayList<Minion>[] cardRows;
    //private ArrayList<Minion> front1 = new ArrayList<>();
    //private ArrayList<Minion> back1 = new ArrayList<>();
    //private ArrayList<Minion> front2 = new ArrayList<>();
    //private ArrayList<Minion> back2 = new ArrayList<>();
    public TableCards() {
        cardRows = new ArrayList[4];
        for (int i = 0; i < cardRows.length; i++) {
            cardRows[i] = new ArrayList<>();
        }
    }

    public ArrayList<Minion> getRow(int IDX){
            return cardRows[IDX];
    }
    public void setRow(int IDX, ArrayList<Minion> row){cardRows[IDX] = row;}

    public void printTable(ObjectMapper objectMapper, ArrayNode outputCorrespondent) {

        for(int i = 0; i < cardRows.length; i++){
            ArrayNode separateRow = objectMapper.createArrayNode();
            ArrayList<Minion> row = cardRows[i];
            for (Minion minion: row) {
                minion.printMinion(separateRow, objectMapper);
            }
            outputCorrespondent.add(separateRow);
        }
    }

    public ArrayNode printFrozenTable(ObjectMapper objectMapper) {
        ArrayNode output = objectMapper.createArrayNode();
        for(int i = 0; i < cardRows.length; i++){
            ArrayList<Minion> row = cardRows[i];
            for (Minion minion: row) {
                if(minion.getIsFrozen() == 1) {
                    minion.printMinion(output, objectMapper);
                }
            }
        }
        return output;
    }

    String cardAttack(Coordinates attacker, Coordinates attacked) {
        Minion atacker = getMinion(attacker);
        Minion atacked = getMinion(attacked);
        if(!areDifferentTeams(attacker.getX(), attacked.getX())) {
            return "Attacked card does not belong to the enemy.";
        }
        if(atacker.getHasAttacked() == 1) {
            return "Attacker card has already attacked this turn.";
        }
        if(atacker.getIsFrozen() == 1) {
            return "Attacker card is frozen.";
        }
        if(checkOpponentTank(attacker) && (!getMinion(attacked).isTank())) {
            return "Attacked card is not of type 'Tank'.";
        }
        atacked.setHealth(atacked.getHealth() - atacker.getAttackDamage());
        if(atacked.getHealth() <= 0) {
            cardRows[attacked.getX()].remove(attacked.getY());
        }
        atacker.setHasAttacked(1);
        return "";
    }

    public String cardUsesAbility(Coordinates attacker, Coordinates attacked) {
        Minion atacker = getMinion(attacker);
        Minion atacked = getMinion(attacked);
        if(atacker.getIsFrozen() == 1) {
            return "Attacker card is Frozen.";
        }
        if(atacker.getHasAttacked() == 1) {
            return "Attacker card has already attacked this turn.";
        }
        String name = atacker.getName();
        if(name.equals("Disciple") && areDifferentTeams(attacker.getX(), attacked.getX())) {
            return "Attacked card does not belong to the current player.";
        }
        if((name.equals("The Ripper") || name.equals("Miraj") || name.equals("The Cursed One"))) {
            if((!areDifferentTeams(attacker.getX(), attacked.getX()))) {
                return "Attacked card does not belong to the enemy.";
            }
            if(checkOpponentTank(attacker) && (!getMinion(attacked).isTank())) {
                return "Attacked card is not of type 'Tank'.";
            }
        }
        atacker.specialAttack(atacked);
        if(atacked.getHealth() <= 0) {
            cardRows[attacked.getX()].remove(attacked.getY());
        }
        atacker.setHasAttacked(1);
        return "";
    }

    String cardAttackHero(Coordinates attacker) {
        Minion atacker = getMinion(attacker);
        if(atacker.getIsFrozen() == 1) {
            return "Attacker card is Frozen.";
        }
        if(atacker.getHasAttacked() == 1) {
            return "Attacker card has already attacked this turn.";
        }
        if(checkOpponentTank(attacker)) {
            return "Attacked card is not of type 'Tank'.";
        }

        return "";
    }

    public String heroAbility(Player player, int playerIDX, int affectedRow) {
        Hero hero = player.getHero();
        String name = hero.getName();
        if(player.getMana() < hero.getMana()) {  /// nu uita sa schimbi mana, hasattacked
            return "Not enough mana to use hero's ability.";
        }
        if(hero.getHasAttacked() == 1) {
            return "Hero has already attacked this turn.";
        }
        if(name.equals("Lord Royce") || name.equals("Empress Thorina")) {
            if(isRowSameTeam(playerIDX, affectedRow)) {
                return "Selected row does not belong to the enemy.";
            }
        }
        if(name.equals("General Kocioraw") || name.equals("King Mudface")) {
            if(!isRowSameTeam(playerIDX, affectedRow)) {
                return "Selected row does not belong to the current player.";
            }
        }
        if(name.equals("Lord Royce")) {
            subZero(affectedRow);
        }
        if(name.equals("Empress Thorina")) {
           lowBlow(affectedRow);
        }
        if(name.equals("General Kocioraw")) {
            bloodThirst(affectedRow);
        }
        if(name.equals("King Mudface")) {
            earthBorn(affectedRow);
        }
        player.setMana(player.getMana() - hero.getMana());
        hero.setHasAttacked(1);
        return "";
    }

    public void clearCards(int player) {
        if(player == 2) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < cardRows[i].size(); j++) {
                    cardRows[i].get(j).setIsFrozen(0);
                    cardRows[i].get(j).setHasAttacked(0);
                }
            }
        }
        else {
            for (int i = 2; i < 4; i++) {
                for (int j = 0; j < cardRows[i].size(); j++) {
                    cardRows[i].get(j).setIsFrozen(0);
                    cardRows[i].get(j).setHasAttacked(0);
                }
            }
        }
    }

    Minion getMinion(Coordinates coordinates) {
        return getRow(coordinates.getX()).get(coordinates.getY());
    }
    boolean checkOpponentTank(Coordinates coordinates) {
        if(coordinates.getX() == 0 || coordinates.getX() == 1) {
            for(int i = 0; i < getRow(2).size(); i++){
                if(getRow(2).get(i).isTank()) {
                    return true;
                }
            }
            return false;
        }
        else {
            for(int i = 0; i < getRow(1).size(); i++){
                if(getRow(1).get(i).isTank()) {
                    return true;
                }
            }
            return false;
        }
    }

    private void subZero(int affectedRow){
        ArrayList<Minion> row = getRow(affectedRow);
        for(int i = 0; i < row.size(); i++){
            row.get(i).setIsFrozen(1);
        }
    }

    private void lowBlow(int affectedRow){
        ArrayList<Minion> row = getRow(affectedRow);
        Minion maxHealthMinion = new Minion();
        int maxHealthIndex = 0;
        for(int i = 0; i < row.size(); i++){
            if(row.get(i).getHealth() > maxHealthMinion.getHealth()) {
                maxHealthMinion = row.get(i);
                maxHealthIndex = i;
            }
        }
        row.remove(maxHealthIndex);
    }

    private void earthBorn(int affectedRow){
        ArrayList<Minion> row = getRow(affectedRow);
        for(int i = 0; i < row.size(); i++){
            row.get(i).setHealth(row.get(i).getHealth() + 1);
        }
    }

    private void bloodThirst(int affectedRow){
        ArrayList<Minion> row = getRow(affectedRow);
        for(int i = 0; i < row.size(); i++){
            row.get(i).setAttackDamage(row.get(i).getAttackDamage() + 1);
        }
    }

    boolean areDifferentTeams(int x1, int x2) {
        boolean t1 = (x1 == 0 || x1 == 1) && (x2 == 0 || x2 == 1);
        boolean t2 = (x1 == 2 || x1 == 3) && (x2 == 2 || x2 == 3);
        return !(t1 || t2);
    }

    boolean isRowSameTeam(int player, int row) {
        boolean t1 = (player == 1 && row == 2) || (player == 1 && row == 3);
        boolean t2 = (player == 2 && row == 0) || (player == 2 && row == 1);
        return (t1 || t2);
    }
}

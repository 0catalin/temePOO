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

    public void clearCards(int player) {
        if(player == 1) {
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
    boolean areDifferentTeams(int x1, int x2) {
        boolean t1 = (x1 == 0 || x1 == 1) && (x2 == 0 || x2 == 1);
        boolean t2 = (x1 == 2 || x1 == 3) && (x2 == 2 || x2 == 3);
        return !(t1 || t2);
    }
}

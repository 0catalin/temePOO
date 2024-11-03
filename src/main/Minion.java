package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

public class Minion extends Card {
    private int attackDamage;
    private int isFrozen;
    public Minion(CardInput card) {
        super(card);
        attackDamage = card.getAttackDamage();
        isFrozen = 0;
    }
    public Minion() {
        super();
    }


    public int getMana() {return super.getMana();}

    public void setMana(int mana) {super.setMana(mana);}
    public void setIsFrozen(int isFrozen) {this.isFrozen = isFrozen;}
    int getIsFrozen() {return isFrozen;}
    public int getHealth() {return super.getHealth();}

    public void setHealth(int health) {super.setHealth(health);}

    public int getAttackDamage() {return attackDamage;}

    public void setAttackDamage(int attackDamage) {this.attackDamage = attackDamage;}

    public String getDescription() {return super.getDescription();}

    public void setDescription(String description) {super.setDescription(description);}
    public void setHasAttacked(int hasAttacked) {super.setHasAttacked(hasAttacked);}
    public int getHasAttacked() {return super.getHasAttacked();}
    public ArrayList<String> getColors() {return super.getColors();}

    public void setColors(ArrayList<String> colors) {super.setColors(colors);}

    public String getName() {return super.getName();}

    public void setName(String name) {super.setName(name);}
    public void printMinion(ArrayNode outputCorrespondent, ObjectMapper objectMapper) {
        ObjectNode nodeInfo = objectMapper.createObjectNode();
        addMinionToObjectNode(nodeInfo, objectMapper);
        outputCorrespondent.add(nodeInfo);
    }

    public void addMinionToObjectNode(ObjectNode nodeInfo, ObjectMapper objectMapper) {
        nodeInfo.put("mana", getMana());
        nodeInfo.put("attackDamage", getAttackDamage());
        nodeInfo.put("health", getHealth());
        nodeInfo.put("description", getDescription());
        ArrayNode colors = objectMapper.createArrayNode();
        for(String s: getColors()) {
            colors.add(s);
        }
        nodeInfo.set("colors", colors);
        nodeInfo.put("name", getName());
    }


    public void specialAttack(Minion attacked) {
        if(getName().equals("The Ripper")) {
            attacked.setAttackDamage(attacked.getAttackDamage() - 2);
            if(attacked.getAttackDamage() < 0)
                attacked.setAttackDamage(0);
        }
        else if(getName().equals("Miraj")) {
            int swap = attacked.getHealth();
            attacked.setHealth(getHealth());
            setHealth(swap);
        }
        else if(getName().equals("The Cursed One")) {
            int swap = attacked.getHealth();
            attacked.setHealth(attacked.getAttackDamage());
            attacked.setAttackDamage(swap);
        }
        else if(getName().equals("Disciple")) {
            attacked.setHealth(attacked.getHealth() + 2);
        }
        else {System.out.println("testul ma pune sa atac cu alt minion sau bug");}
    }

    public String getRow() {
        if(getName().equals("Sentinel") || getName().equals("Berserker") || getName().equals("The Cursed One") || getName().equals("Disciple")) {
            return "back";
        }
        return "front";
    }
    public boolean isTank() {
        return (getName().equals("Goliath") || getName().equals("Warden"));
    }
}

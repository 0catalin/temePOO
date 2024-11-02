package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

public class Minion extends Card {
    private int attackDamage;

    public Minion(CardInput card) {
        super(card);
        attackDamage = card.getAttackDamage();
    }


    public int getMana() {return super.getMana();}

    public void setMana(int mana) {super.setMana(mana);}

    public int getHealth() {return super.getHealth();}

    public void setHealth(int health) {super.setHealth(health);}

    public int getAttackDamage() {return attackDamage;}

    public void setAttackDamage(int attackDamage) {this.attackDamage = attackDamage;}

    public String getDescription() {return super.getDescription();}

    public void setDescription(String description) {super.setDescription(description);}

    public ArrayList<String> getColors() {return super.getColors();}

    public void setColors(ArrayList<String> colors) {super.setColors(colors);}

    public String getName() {return super.getName();}

    public void setName(String name) {super.setName(name);}
    public void printMinion(ArrayNode outputCorrespondent, ObjectMapper objectMapper) {
        ObjectNode nodeInfo = objectMapper.createObjectNode();
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
        outputCorrespondent.add(nodeInfo);
    }

    public String getRow() {
        if(getName().equals("Sentinel") || getName().equals("Berserker") || getName().equals("The Cursed One") || getName().equals("Disciple")) {
            return "back";
        }
        return "front";
    }

}

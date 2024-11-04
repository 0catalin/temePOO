package main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class Minion extends Card {
    private int attackDamage;
    private boolean isFrozen;
    public Minion(final CardInput card) {
        super(card);
        attackDamage = card.getAttackDamage();
        isFrozen = false;
    }

    public Minion() {
        super();
    }

    public void setIsFrozen(final boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    boolean getIsFrozen() {
        return isFrozen;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }


    public void printMinion(final ArrayNode outputCorrespondent, final ObjectMapper mapper) {
        ObjectNode nodeInfo = mapper.createObjectNode();
        addMinionToObjectNode(nodeInfo, mapper);
        outputCorrespondent.add(nodeInfo);
    }

    public void addMinionToObjectNode(final ObjectNode nodeInfo, final ObjectMapper mapper) {
        nodeInfo.put("mana", getMana());
        nodeInfo.put("attackDamage", getAttackDamage());
        nodeInfo.put("health", getHealth());
        nodeInfo.put("description", getDescription());
        ArrayNode colors = mapper.createArrayNode();
        for (String s: getColors()) {
            colors.add(s);
        }
        nodeInfo.set("colors", colors);
        nodeInfo.put("name", getName());
    }


    public void specialAttack(final Minion attacked) {
        if (getName().equals("The Ripper")) {
            attacked.setAttackDamage(attacked.getAttackDamage() - 2);
            if (attacked.getAttackDamage() < 0) {
                attacked.setAttackDamage(0);
            }
        } else if (getName().equals("Miraj")) {
            int swap = attacked.getHealth();
            attacked.setHealth(getHealth());
            setHealth(swap);
        } else if (getName().equals("The Cursed One")) {
            int swap = attacked.getHealth();
            attacked.setHealth(attacked.getAttackDamage());
            attacked.setAttackDamage(swap);
        } else if (getName().equals("Disciple")) {
            attacked.setHealth(attacked.getHealth() + 2);
        } else {
            System.out.println("testul ma pune sa atac cu alt minion sau bug");
        }
    }

    public String getRow() {
        if (getName().equals("Sentinel") || getName().equals("Berserker")) {
            return "back";
        }
        if (getName().equals("The Cursed One") || getName().equals("Disciple")) {
            return "back";
        }
        return "front";
    }
    public boolean isTank() {
        return (getName().equals("Goliath") || getName().equals("Warden"));
    }
}

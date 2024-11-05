package characters;

import fileio.CardInput;

import java.util.ArrayList;

/**
 * This class represents a generic card and is designed to be extended by Minion and Hero cards.
 * It serves as a base card for any card : has the fields mana, health, description,
 * colors, name and hasAttacked (which is true if it has attacked the current round before)
 */
public class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors = new ArrayList<String>();
    private String name;
    private boolean hasAttacked;
    private static final int HERO_HEALTH = 30;

    public Card(final CardInput input) {
        mana = input.getMana();
        if (input.getHealth() == 0) {
            health = HERO_HEALTH;
        } else {
            health = input.getHealth();
        }
        description = input.getDescription();
        for (int i = 0; i < input.getColors().size(); i++) {
            colors.add(new String(input.getColors().get(i)));
        }
        name = input.getName();
        hasAttacked = false;
    }
    public Card() {
        health = 0;
    }
    public final int getMana() {
        return mana;
    }
    public final void setMana(final int mana) {
        this.mana = mana;
    }
    public final int getHealth() {
        return health;
    }
    public final void setHealth(final int health) {
        this.health = health;
    }
    public final String getDescription() {
        return description;
    }
    public final void setDescription(final String description) {
        this.description = description;
    }
    public final ArrayList<String> getColors() {
        return colors;
    }
    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }
    public final String getName() {
        return name;
    }
    public final void setName(final String name) {
        this.name = name;
    }
    public final boolean getHasAttacked() {
        return hasAttacked;
    }
    public final void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
}

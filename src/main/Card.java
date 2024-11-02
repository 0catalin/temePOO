package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors = new ArrayList<>();
    private String name;
    private int hasAttacked;

    public Card(CardInput input) {
        mana = input.getMana();
        if (input.getHealth() == 0) {health = 30;}
        else { health = input.getHealth(); }
        description = input.getDescription();
        //colors = input.getColors(); // posibil sa fie nevoie sa dau add din moment ce am alocat mai sus
        for(int i = 0; i < input.getColors().size(); i++) {
            colors.add(new String(input.getColors().get(i)));
        }
        name = input.getName();
        hasAttacked = 0;
    }
    public Card(int mana, int health, String description, String colors, String name) {}
    public int getMana() {return mana;}
    public void setMana(int mana) {this.mana = mana;}
    public int getHealth() {return health;}
    public void setHealth(int health) {this.health = health;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public ArrayList<String> getColors() {return colors;}
    public void setColors(ArrayList<String> colors) {this.colors = colors;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getHasAttacked() {return hasAttacked;}
    public void setHasAttacked(int hasAttacked) {this.hasAttacked = hasAttacked;}

}

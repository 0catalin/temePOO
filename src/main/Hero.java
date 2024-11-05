package main;

import fileio.CardInput;

/**
 * The hero class is simply a subclass of the Card class
 */
public final class Hero extends Card {
    public Hero(final CardInput card) {
        super(card);
    }

    /**
     * The method simulates an attack on the hero by a certain Minion
     * @param player the id of the player
     * @param attackCard the minion who attacks the hero
     * @param stats the object storing the game statistics
     * @return
     */
    public String attackTheHero(final int player, final Minion attackCard, final Statistics stats) {
        setHealth(getHealth() - attackCard.getAttackDamage());
        attackCard.setHasAttacked(true);
        if (getHealth() > 0) {
            return "";
        }
        if (player == 1) {
            stats.setPlayer1Wins(stats.getPlayer1Wins() + 1);
            stats.setTotalGamesPlayed(stats.getTotalGamesPlayed() + 1);
            return "Player one killed the enemy hero.";
        }
        stats.setPlayer2Wins(stats.getPlayer2Wins() + 1);
        stats.setTotalGamesPlayed(stats.getTotalGamesPlayed() + 1);
        return "Player two killed the enemy hero.";
    }
}



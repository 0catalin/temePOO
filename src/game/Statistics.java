package game;

/**
 * the Statistics class stores the game statistics
 */
public final class Statistics {
    private int totalGamesPlayed;
    private int player1Wins;
    private int player2Wins;
    public Statistics() { }
    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }
    public int getPlayer1Wins() {
        return player1Wins;
    }
    public int getPlayer2Wins() {
        return player2Wins;
    }
    public void setTotalGamesPlayed(final int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }
    public void setPlayer1Wins(final int player1Wins) {
        this.player1Wins = player1Wins;
    }
    public void setPlayer2Wins(final int player2Wins) {
        this.player2Wins = player2Wins;
    }
}

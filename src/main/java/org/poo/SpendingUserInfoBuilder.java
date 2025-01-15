package org.poo;

/**
 * class used as a builder for the spendingUserInfo class
 */
public final class SpendingUserInfoBuilder {

    private double deposited = 0;
    private double spent = 0;
    private final String email;
    private final int timestamp;
    private String commerciant = null;



    public SpendingUserInfoBuilder(final String email, final int timestamp) {
        this.email = email;
        this.timestamp = timestamp;
    }


    /**
     * sets the deposited variable
     * @param depositedAmount the deposited amount
     * @return the builder instance
     */
    public SpendingUserInfoBuilder deposited(final double depositedAmount) {
        this.deposited = depositedAmount;
        return this;
    }



    /**
     * sets the spent variable
     * @param spentAmount the spent amount
     * @return the builder instance
     */
    public SpendingUserInfoBuilder spent(final double spentAmount) {
        this.spent = spentAmount;
        return this;
    }



    /**
     * sets the commerciant variable
     * @param newCommerciant the commerciant
     * @return the builder instance
     */
    public SpendingUserInfoBuilder commerciant(final String newCommerciant) {
        this.commerciant = newCommerciant;
        return this;
    }



    /**
     * builds a SpendingUserInfo instance with the accumulated variables
     * @return the SpendingUserInfo instance created
     */
    public SpendingUserInfo build() {
        return new SpendingUserInfo(deposited, spent, email, timestamp, commerciant);
    }
}


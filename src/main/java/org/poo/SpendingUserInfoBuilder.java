package org.poo;

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

    public SpendingUserInfoBuilder deposited(final double depositedAmount) {
        this.deposited = depositedAmount;
        return this;
    }

    public SpendingUserInfoBuilder spent(final double spentAmount) {
        this.spent = spentAmount;
        return this;
    }



    public SpendingUserInfoBuilder commerciant(final String newCommerciant) {
        this.commerciant = newCommerciant;
        return this;
    }

    public SpendingUserInfo build() {
        return new SpendingUserInfo(deposited, spent, email, timestamp, commerciant);
    }
}


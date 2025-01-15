package org.poo;

public class SpendingUserInfoBuilder {
    private double deposited = 0;
    private double spent = 0;
    private String email;
    private int timestamp;
    private String commerciant = null;

    public SpendingUserInfoBuilder (String email, int timestamp) {
        this.email = email;
        this.timestamp = timestamp;
    }

    public SpendingUserInfoBuilder deposited(double deposited) {
        this.deposited = deposited;
        return this;
    }

    public SpendingUserInfoBuilder spent(double spent) {
        this.spent = spent;
        return this;
    }

    public SpendingUserInfoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public SpendingUserInfoBuilder timestamp(int timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SpendingUserInfoBuilder commerciant(String commerciant) {
        this.commerciant = commerciant;
        return this;
    }

    public SpendingUserInfo build() {
        return new SpendingUserInfo(deposited, spent, email, timestamp, commerciant);
    }
}


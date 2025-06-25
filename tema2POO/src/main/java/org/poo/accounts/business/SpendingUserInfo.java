package org.poo.accounts.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class SpendingUserInfo {

    private final double deposited;
    private final double spent;
    private final String email;
    private final int timestamp;
    private final String commerciant;


    public SpendingUserInfo(final double deposited,
                            final double spent, final String email,
                            final int timestamp, final String commerciant) {
        this.deposited = deposited;
        this.spent = spent;
        this.email = email;
        this.timestamp = timestamp;
        this.commerciant = commerciant;
    }


    /**
     * class used as a builder for the spendingUserInfo class
     */
    public static class Builder {

        private double deposited = 0;
        private double spent = 0;
        private final String email;
        private final int timestamp;
        private String commerciant = null;



        public Builder(final String email, final int timestamp) {
            this.email = email;
            this.timestamp = timestamp;
        }


        /**
         * sets the deposited variable
         * @param depositedAmount the deposited amount
         * @return the builder instance
         */
        public Builder deposited(final double depositedAmount) {
            this.deposited = depositedAmount;
            return this;
        }



        /**
         * sets the spent variable
         * @param spentAmount the spent amount
         * @return the builder instance
         */
        public Builder spent(final double spentAmount) {
            this.spent = spentAmount;
            return this;
        }



        /**
         * sets the commerciant variable
         * @param newCommerciant the commerciant
         * @return the builder instance
         */
        public Builder commerciant(final String newCommerciant) {
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

}

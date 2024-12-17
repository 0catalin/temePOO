package org.poo.accounts;

import org.poo.visitors.reportVisitors.Visitor;


/**
 * class designed to extend Account class, representing a savings account entity
 */
public final class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount(final String currency, final double interestRate) {
        super(currency);
        this.interestRate = interestRate;
        setType("savings");
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * method of accepting the visitor
     * @param visitor the interface of the possible visitor classes
     */
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }

}

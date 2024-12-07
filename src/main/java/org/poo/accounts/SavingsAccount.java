package org.poo.accounts;

import org.poo.visitors.reportVisitors.Visitor;
import org.poo.utils.Utils;

import java.util.ArrayList;



public final class SavingsAccount extends Account {
    private double interestRate;
    public SavingsAccount(final String currency, final double interestRate) {
        setCurrency(currency);
        this.interestRate = interestRate;
        setBalance(0);
        setCards(new ArrayList<>());
        setAlias("");
        setIban(Utils.generateIBAN());
        setType("savings");
        setSpendingReports(new ArrayList<>());
        setReportsSavings(new ArrayList<>());
        setReportsClassic(new ArrayList<>());
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

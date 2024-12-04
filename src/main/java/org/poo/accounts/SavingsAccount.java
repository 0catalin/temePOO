package org.poo.accounts;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.SpendingsReportVisitor;
import org.poo.utils.Utils;

import java.util.ArrayList;



public class SavingsAccount extends Account{
    private double interestRate;
    public SavingsAccount(String currency, double interestRate) {
        setCurrency(currency);
        this.interestRate = interestRate;
        setBalance(0);
        setCards(new ArrayList<>());
        setAlias("");
        setIBAN(Utils.generateIBAN());
        setType("savings");
    }

    public double getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void accept(SpendingsReportVisitor visitor) {
        visitor.visit(this);
    }



}

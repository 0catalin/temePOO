package org.poo.baseinput;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

@Getter
@Setter

public class ExchangeRate {
    private String from;
    private String to;
    private double rate;

    public ExchangeRate(final ExchangeInput exchangeInput) {
        from = exchangeInput.getFrom();
        to = exchangeInput.getTo();
        rate = exchangeInput.getRate();
    }

    public ExchangeRate(final ExchangeRate exchangeRate) {
        from = exchangeRate.getTo();
        to = exchangeRate.getFrom();
        rate = 1 / exchangeRate.getRate();
    }

    public ExchangeRate(final String from, final double rate, final String to) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public ExchangeRate(final ExchangeRate exchangeRate, final ExchangeRate exchangeRate2) {
        this.from = exchangeRate.getFrom();
        this.to = exchangeRate2.getTo();
        this.rate = exchangeRate.getRate() * exchangeRate2.getRate();
    }
}

package org.poo.baseinput;
import lombok.Getter;
import lombok.Setter;
import org.poo.parsers.fileio.ExchangeInput;

@Getter
@Setter

/**
 * class designed to store an input exchange rate
 */
public final class ExchangeRate {
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

}

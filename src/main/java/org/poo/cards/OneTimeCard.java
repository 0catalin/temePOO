package org.poo.cards;

import org.poo.utils.Utils;

public class OneTimeCard extends Card{
    public OneTimeCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }
}

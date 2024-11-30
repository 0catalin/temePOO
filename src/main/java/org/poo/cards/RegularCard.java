package org.poo.cards;

import org.poo.utils.Utils;

public class RegularCard extends Card{
    public RegularCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }
}

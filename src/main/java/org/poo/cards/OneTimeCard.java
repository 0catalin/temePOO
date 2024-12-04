package org.poo.cards;

import org.poo.PayOnlineVisitor;
import org.poo.utils.Utils;

public class OneTimeCard extends Card{
    public OneTimeCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }

    public boolean accept(PayOnlineVisitor visitor) {
        return visitor.visit(this);
    }
}

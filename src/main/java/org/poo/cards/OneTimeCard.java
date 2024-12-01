package org.poo.cards;

import org.poo.PayOnlineVisitor;
import org.poo.utils.Utils;

public class OneTimeCard extends Card{
    public OneTimeCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }

    public void accept(PayOnlineVisitor visitor) {
        visitor.visit(this);
    }
}

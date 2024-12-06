package org.poo.cards;

import org.poo.PayOnlineVisitor;
import org.poo.utils.Utils;

public class RegularCard extends Card{
    public RegularCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }

    public void accept(PayOnlineVisitor visitor) {
        visitor.visit(this);
    }

}

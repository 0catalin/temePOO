package org.poo.cards;

import org.poo.visitors.PayOnlineVisitor;
import org.poo.utils.Utils;

public final class OneTimeCard extends Card {
    public OneTimeCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }

    public void accept(final PayOnlineVisitor visitor) {
        visitor.visit(this);
    }
}

package org.poo.cards;

import org.poo.visitors.PayOnlineVisitor;
import org.poo.utils.Utils;

public final class RegularCard extends Card {
    public RegularCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }

    public void accept(final PayOnlineVisitor visitor) {
        visitor.visit(this);
    }

}

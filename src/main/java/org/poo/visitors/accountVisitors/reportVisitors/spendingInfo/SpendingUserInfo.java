package org.poo.visitors.accountVisitors.reportVisitors.spendingInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class SpendingUserInfo {

    private final double deposited;
    private final double spent;
    private final String email;
    private final int timestamp;
    private final String commerciant;


    public SpendingUserInfo(final double deposited,
                            final double spent, final String email,
                            final int timestamp, final String commerciant) {
        this.deposited = deposited;
        this.spent = spent;
        this.email = email;
        this.timestamp = timestamp;
        this.commerciant = commerciant;
    }

}

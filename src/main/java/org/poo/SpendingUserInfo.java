package org.poo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpendingUserInfo {

    private double deposited;
    private double spent;
    private String email;
    private int timestamp;


    public SpendingUserInfo(double deposited, double spent, String email, int timestamp) {
        this.deposited = deposited;
        this.spent = spent;
        this.email = email;
        this.timestamp = timestamp;
    }

}

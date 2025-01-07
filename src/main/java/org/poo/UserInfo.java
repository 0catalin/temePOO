package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;


@Getter
@Setter
public class UserInfo {
    private String username;
    private String email;

    public UserInfo(String email) {
        this.email = email;
        User user = Bank.getInstance().getUserByEmail(email);
        username = user.getLastName() + " " + user.getFirstName();
    }
}

package org.poo.accounts.business;

import lombok.Getter;
import lombok.Setter;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;


@Getter
@Setter

/**
 * class designed to store a user's email and his name
 */
public class UserInfo {

    private String username;
    private String email;

    public UserInfo(final String email) {
        this.email = email;
        User user = Bank.getInstance().getUserByEmail(email);
        username = user.getLastName() + " " + user.getFirstName();
    }
}

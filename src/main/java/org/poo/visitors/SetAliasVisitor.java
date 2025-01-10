package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.baseinput.User;
import org.poo.visitors.reportVisitors.Visitor;

public class SetAliasVisitor implements Visitor {
    private User user;
    private String alias;
    private String email;
    private int timestamp;
    private String iban;

    public SetAliasVisitor(User user, String alias, String email, int timestamp, String iban) {

        this.user = user;
        this.alias = alias;
        this.email = email;
        this.timestamp = timestamp;
        this.iban = iban;

    }



    public void visit(ClassicAccount account) {
            if (user.getAccounts().contains(account)) {
                account.setAlias(alias);
            }
    }



    public void visit(BusinessAccount account) {

        if (!account.getEmailToCards().containsKey(email)) {

        } else if (!account.getRbac().hasPermissions(user.getEmail(), "setAlias")) {

        } else {
            account.setAlias(alias);
        }
    }



    public void visit(SavingsAccount account) {
        if (user.getAccounts().contains(account)) {
            account.setAlias(alias);
        }
    }
}

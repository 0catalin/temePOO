package org.poo.visitors;

import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.baseinput.User;
import org.poo.visitors.reportVisitors.Visitor;



/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class SetAliasVisitor implements Visitor {
    private final User user;
    private final String alias;
    private final String email;

    public SetAliasVisitor(final User user, final String alias,
                           final String email) {

        this.user = user;
        this.alias = alias;
        this.email = email;

    }


    /**
     * sets alias of classic account
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {
            if (user.getAccounts().contains(account)) {
                account.setAlias(alias);
            }
    }


    /**
     * is the user has permissions the alias is changed
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {

        if (!account.getEmailToCards().containsKey(email)) {
            return;
        } else if (!account.getRbac().hasPermissions(user.getEmail(), "setAlias")) {
            return;
        } else {
            account.setAlias(alias);
        }
    }



    /**
     * sets alias to savings account
     * @param account the savings account
     */
    public void visit(final SavingsAccount account) {
        if (user.getAccounts().contains(account)) {
            account.setAlias(alias);
        }
    }
}

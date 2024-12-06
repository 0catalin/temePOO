package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.cards.RegularCard;
import org.poo.fileio.CommandInput;

public class SetAlias implements Command{
    private String IBAN;
    private String email;
    private String alias;

    public SetAlias(CommandInput commandInput) {
        email = commandInput.getEmail();
        IBAN = commandInput.getAccount();
        alias = commandInput.getAlias();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        User user = Bank.getInstance().getUserByEmail(email);
        if (account == null) {
         // don't have to do anything with exception
        } else if (user == null) {
            // don't have to do anything with exception
        } else if (user.getAccounts().contains(account)) {
            account.setAlias(alias);
        }
    }

}

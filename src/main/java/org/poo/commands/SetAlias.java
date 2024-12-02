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

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        User user = bank.getUserByEmail(email);
        if (account == null) {
            System.out.println("Account not found");
        } else if (user == null) {
            System.out.println("User not found");
        } else {
            if (user.getAccounts().contains(account)) {
                account.setAlias(alias);
            } else {
                System.out.println("Account does not belong to the user");
            }
        }
    }
}

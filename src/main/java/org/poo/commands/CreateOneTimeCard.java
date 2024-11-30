package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.cards.OneTimeCard;
import org.poo.cards.RegularCard;
import org.poo.fileio.CommandInput;

public class CreateOneTimeCard implements Command{
    private String IBAN;
    private String email;
    private int timestamp;

    public CreateOneTimeCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        IBAN = commandInput.getAccount();
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
                account.getCards().add(new OneTimeCard());
            } else {
                System.out.println("Account does not belong to the user");
            }
        }
    }
}
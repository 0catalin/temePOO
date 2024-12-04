package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;

public class SetMinimumBalance implements Command{
    private String IBAN;
    private double minBalance;
    private int timestamp;

    public SetMinimumBalance(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        IBAN = commandInput.getAccount();
        minBalance = commandInput.getAmount();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        if (account != null) {
            account.setMinBalance(minBalance);
        } else {
            System.out.println("Account not found");
        }
    }
}
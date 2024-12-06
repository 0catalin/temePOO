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

    public SetMinimumBalance(CommandInput commandInput) {
        IBAN = commandInput.getAccount();
        minBalance = commandInput.getAmount();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByIBAN(IBAN);
        if (account != null) {
            account.setMinBalance(minBalance);
        } else {
            // don't have to handle exception
        }
    }
}
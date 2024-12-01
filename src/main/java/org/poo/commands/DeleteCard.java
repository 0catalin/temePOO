package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;

public class DeleteCard implements Command{
    private String cardNumber;
    private int timestamp;

    public DeleteCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByCardNumber(cardNumber);
        if(account == null) {
            System.out.println("Card not found");
        } else {
            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        }

    }
}
package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class DeleteCard implements Command{
    private String cardNumber;
    private int timestamp;

    public DeleteCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }

    public void execute(Bank bank, ArrayNode output) {

    }
}
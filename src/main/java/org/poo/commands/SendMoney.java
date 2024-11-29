package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class SendMoney implements Command{
    private double amount;
    private int timestamp;
    private String account;
    private String description;
    private String receiver;

    public SendMoney(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        account = commandInput.getAccount();
        description = commandInput.getDescription();
        receiver = commandInput.getReceiver();
    }

    public void execute(Bank bank, ArrayNode output) {

    }
}

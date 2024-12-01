package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;

public class SendMoney implements Command{
    private double amount;
    private int timestamp;
    private String IBAN;
    private String description;
    private String receiver;

    public SendMoney(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        amount = commandInput.getAmount();
        IBAN = commandInput.getAccount();
        description = commandInput.getDescription();
        receiver = commandInput.getReceiver();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account accountSender = bank.getAccountByIBAN(IBAN);
        Account accountReceiver = bank.getAccountByIBAN(receiver);
        if(accountSender == null || accountReceiver == null) {
            System.out.println("Account not found");
        } else if (accountSender.getBalance() < amount) {
            System.out.println("Insufficient balance");
        } else {
            accountSender.setBalance(accountSender.getBalance() - amount);
            accountReceiver.setBalance(accountReceiver.getBalance() +
                    amount * bank.findExchangeRate(accountSender.getCurrency(),
                            accountReceiver.getCurrency()));
        }
    }


}

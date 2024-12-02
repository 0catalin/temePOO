package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
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
        Account accountReceiver = bank.getAccountByIBANOrAlias(receiver);
        User user = bank.getUserByIBAN(IBAN);
        if(accountSender == null || accountReceiver == null) {
            System.out.println("Account not found");
        } else if (accountSender.getBalance() < amount) {
            System.out.println("Insufficient balance");
        } else {
            user.getTranzactions().add(addToUsersTranzactions(mapper, accountSender, accountReceiver));
            accountSender.setBalance(accountSender.getBalance() - amount);
            accountReceiver.setBalance(accountReceiver.getBalance() +
                    amount * bank.findExchangeRate(accountSender.getCurrency(),
                            accountReceiver.getCurrency()));

        }
    }

    private ObjectNode addToUsersTranzactions(ObjectMapper mapper, Account accountSender, Account accountReceiver) {
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", description);
        output.put("senderIBAN", IBAN);
        output.put("receiverIBAN", accountReceiver.getIBAN());
        output.put("amount", "" + amount + " " + accountSender.getCurrency());
        output.put("transferType", "sent");
        return output;
    }

}

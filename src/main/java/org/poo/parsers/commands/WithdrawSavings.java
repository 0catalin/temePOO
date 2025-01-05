package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;

public class WithdrawSavings implements Command{
    private String iban;
    private double amount;
    private String currency;
    private int timestamp;

    public WithdrawSavings(CommandInput commandInput) {
        iban = commandInput.getAccount();
        amount = commandInput.getAmount();
        currency = commandInput.getCurrency();
        timestamp = commandInput.getTimestamp();
    }

    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByIBAN(iban);


            if (!user.isUserOldEnough()) {
                user.getTranzactions().add(ageTooLow());
            } else if (user.getClassicAccounts().isEmpty()) {
                user.getTranzactions().add(noClassicAccount());
            } else if (!account.getType().equals("savings")) {
                // account not of type savings
            } else {
                Account accountReceiver = user.getClassicAccounts().get(0);
                if (account.getBalance() - account.getMinBalance() > amount * Bank.getInstance().findExchangeRate(currency, account.getCurrency())) {
                    amount = amount * Bank.getInstance().findExchangeRate(currency, account.getCurrency());
                    account.setBalance(account.getBalance() - amount);

                    amount = amount * Bank.getInstance().findExchangeRate(account.getCurrency(), accountReceiver.getCurrency());
                    accountReceiver.setBalance(accountReceiver.getBalance() + amount);
                } else {
                    // not enough balance
                }
            }
        } catch (AccountNotFoundException e) {

        }
    }

    private ObjectNode ageTooLow() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "You don't have the minimum age required.");
        return output;
    }

    private ObjectNode noClassicAccount() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("description", "You do not have a classic account.");
        output.put("timestamp", timestamp);
        return output;
    }
}

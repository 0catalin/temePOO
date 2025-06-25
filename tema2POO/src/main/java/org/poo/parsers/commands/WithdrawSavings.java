package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;

public final class WithdrawSavings implements Command {
    private final String iban;
    private double amount;
    private final String currency;
    private final int timestamp;

    public WithdrawSavings(final CommandInput commandInput) {
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
                account.getReportsSavings().add(noClassicAccount());
            } else if (!account.getType().equals("savings")) {
                return;
            } else {

                Account accountReceiver = user.getClassicAccounts().getFirst();
                if (account.getBalance() >= amount * Bank.getInstance().findExchangeRate(currency,
                        account.getCurrency())) {
                    double initialAmount = amount;
                    amount = amount * Bank.getInstance().findExchangeRate(currency,
                            account.getCurrency());
                    account.setBalance(account.getBalance() - amount);

                    amount = amount * Bank.getInstance().findExchangeRate(account.getCurrency(),
                            accountReceiver.getCurrency());
                    accountReceiver.setBalance(accountReceiver.getBalance() + amount);
                    user.getTranzactions().add(savingsWithdrawal(accountReceiver.getIban(),
                            account.getIban(), initialAmount));
                    user.getTranzactions().add(savingsWithdrawal(accountReceiver.getIban(),
                            account.getIban(), initialAmount));
                } else {
                    // not enough balance
                    return;
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

    private ObjectNode savingsWithdrawal(final String ibanClassic,
                                         final String ibanSavings,
                                         final double initialAmount) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("amount", initialAmount);
        output.put("classicAccountIBAN", ibanClassic);
        output.put("description", "Savings withdrawal");
        output.put("savingsAccountIBAN", ibanSavings);
        output.put("timestamp", timestamp);
        return output;
    }
}

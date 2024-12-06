package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public final class SplitPayment implements Command {
    private double amount;
    private int timestamp;
    private String currency;
    private List<String> accountsForSplit;

    public SplitPayment(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        timestamp = commandInput.getTimestamp();
        currency = commandInput.getCurrency();
        accountsForSplit = commandInput.getAccounts();
    }

    @Override
    public void execute() {
        ArrayList<Account> accountList = new ArrayList<Account>();
        ArrayList<User> userList = new ArrayList<User>();
        for (String iban : accountsForSplit) {
            accountList.add(Bank.getInstance().getAccountByIBAN(iban));
            userList.add(Bank.getInstance().getUserByIBAN(iban));
        }
        String problemIban = "";
        double eachAmount = amount / accountList.size(); // amount per each
        for (Account account : accountList) {
            if (account.getBalance() - eachAmount * Bank.getInstance()
                    .findExchangeRate(currency, account.getCurrency()) < account.getMinBalance()) {
                problemIban = account.getIban();
            }
        }
        if (problemIban.isEmpty()) {
            for (int i = 0; i < accountList.size(); i++) {
                userList.get(i).getTranzactions().add(splitPayment());
                accountList.get(i).getReportsClassic().add(splitPayment());

                Bank.getInstance().getAccountByIBAN(accountsForSplit.get(i))
                        .setBalance(accountList.get(i).getBalance()
                                - eachAmount * Bank.getInstance()
                                .findExchangeRate(currency, accountList.get(i).getCurrency()));
            }
        } else {
            ObjectNode node = splitPayment();
            node.put("error", "Account " + problemIban
                    +  " has insufficient funds for a split payment.");
            for (int i = 0; i < userList.size(); i++) {
                userList.get(i).getTranzactions().add(node);
                accountList.get(i).getReportsClassic().add(node);
            }
        }
    }

    private ObjectNode splitPayment() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode successNode = mapper.createObjectNode();
        successNode.put("timestamp", timestamp);
        successNode.put("description",
                "Split payment of " + String.format("%.2f", amount) + " " + currency);
        successNode.put("currency", currency);
        successNode.put("amount", amount / accountsForSplit.size());
        ArrayNode accountsNode = mapper.createArrayNode();
        for (String iban : accountsForSplit) {
            accountsNode.add(iban);
        }
        successNode.set("involvedAccounts", accountsNode);
        return successNode;
    }
}

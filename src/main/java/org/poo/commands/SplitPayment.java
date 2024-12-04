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

public class SplitPayment implements Command{
    private double amount;
    private int timestamp;
    private String currency;
    private List<String> accountsForSplit;

    public SplitPayment(CommandInput commandInput) {
        amount = commandInput.getAmount();
        timestamp = commandInput.getTimestamp();
        currency = commandInput.getCurrency();
        accountsForSplit = commandInput.getAccounts();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        ArrayList<Account> accountList = new ArrayList<Account>();
        ArrayList<User> userList = new ArrayList<>();
        for (String IBAN : accountsForSplit ) {
            accountList.add(bank.getAccountByIBAN(IBAN));
            userList.add(bank.getUserByIBAN(IBAN));
        }
        String problemIBAN = "";
        double eachAmount = amount / accountList.size(); // amount per each
        for (Account account : accountList) {
            if(account.getBalance() - eachAmount * bank.findExchangeRate(currency, account.getCurrency()) < account.getMinBalance()) {
                problemIBAN = account.getIBAN();
            }
        }
        if(problemIBAN.isEmpty()) {
            for (int i = 0; i < accountList.size(); i++) {
                userList.get(i).getTranzactions().add(splitPayment(mapper));
                bank.getAccountByIBAN(accountsForSplit.get(i)).setBalance(accountList.get(i).getBalance() -
                        eachAmount * bank.findExchangeRate(currency, accountList.get(i).getCurrency()));
                // testul gresit, trebuie inversate alea de currency daca se modifica testul
            }
        } else {
            ObjectNode node = splitPayment(mapper);
            node.put("error", "Account " + problemIBAN +  " has insufficient funds for a split payment.");
            for (int i = 0; i < userList.size(); i++) {
                userList.get(i).getTranzactions().add(node);
                // bank.getMap2().put(userList.get(i).getTranzactions().size(), accountList.get(i).getIBAN());
            }
        }
    }

    public ObjectNode splitPayment(ObjectMapper mapper) {
        ObjectNode successNode = mapper.createObjectNode();
        successNode.put("timestamp", timestamp);
        successNode.put("description", "Split payment of " + String.format("%.2f", amount) + " " + currency);
        successNode.put("currency", currency);
        successNode.put("amount", amount / accountsForSplit.size());
        ArrayNode accountsNode = mapper.createArrayNode();
        for (String IBAN : accountsForSplit) {
            accountsNode.add(IBAN);
        }
        successNode.set("involvedAccounts", accountsNode);
        return successNode;
    }
}
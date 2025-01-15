package org.poo.splitPayment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.PayAllObserver;

import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.EmailNotFoundException;
import org.poo.parsers.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;


@Getter
@Setter
public class SplitPaymentInfo {
    private final double amount;
    private final int timestamp;
    private final String currency;
    private final List<String> accountsForSplit;
    private final PayAllObserver observer;
    private final String splitPaymentType;
    private final Set<String> emailsForSplit;


    public SplitPaymentInfo(final CommandInput commandInput) {
        amount = commandInput.getAmount();
        timestamp = commandInput.getTimestamp();
        currency = commandInput.getCurrency();
        accountsForSplit = commandInput.getAccounts();
        observer = new PayAllObserver(accountsForSplit);
        splitPaymentType = commandInput.getSplitPaymentType();

        emailsForSplit = new HashSet<String>();
        for (String iban : accountsForSplit) {
            try {
                emailsForSplit.add(Bank.getInstance().getEmailByIban(iban));
            } catch (EmailNotFoundException ignored) {

            }
        }
    }



    public boolean isRightType(final String paymentType, final String email) {
        return splitPaymentType.equals(paymentType) && emailsForSplit.contains(email);
    }



    public void successfulPayment() {
        try {
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
                        .findExchangeRate(currency,
                                account.getCurrency()) < account.getMinBalance()) {
                    problemIban = account.getIban();
                    break;
                }
            }
            if (problemIban.isEmpty()) {
                for (int i = 0; i < accountList.size(); i++) {
                    userList.get(i).getTranzactions().add(splitPayment());
                    userList.get(i).checkFivePayments(eachAmount * Bank.getInstance()
                            .findExchangeRate(currency, "RON"),
                            accountList.get(i).getIban(), timestamp);
                    userList.get(i).getTranzactions().sort(
                            Comparator.comparingInt(t -> t.get("timestamp").asInt()));
                    accountList.get(i).getReportsClassic().add(splitPayment());

                    Bank.getInstance().getAccountByIBAN(accountsForSplit.get(i))
                            .setBalance(accountList.get(i).getBalance()
                                    - eachAmount * Bank.getInstance()
                                    .findExchangeRate(currency, accountList.get(i).getCurrency()));
                }
            } else {
                ObjectNode node = splitPayment();
                node.put("error", "Account " + problemIban
                        + " has insufficient funds for a split payment.");
                for (int i = 0; i < userList.size(); i++) {
                    userList.get(i).getTranzactions().add(node);
                    userList.get(i).getTranzactions().sort(
                            Comparator.comparingInt(t -> t.get("timestamp").asInt()));
                    accountList.get(i).getReportsClassic().add(node);
                }
            }
        } catch (AccountNotFoundException e) {

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
        successNode.put("splitPaymentType", splitPaymentType);
        return successNode;
    }



    public ObjectNode rejectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        return node;
    }

}

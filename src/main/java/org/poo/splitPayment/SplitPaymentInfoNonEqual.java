package org.poo.splitPayment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Getter
@Setter
public class SplitPaymentInfoNonEqual extends SplitPaymentInfo {
    private ArrayList<Double> amountsForUsers;
    public SplitPaymentInfoNonEqual(CommandInput commandInput) {
        super(commandInput);
        amountsForUsers = (ArrayList<Double>) commandInput.getAmountForUsers();
    }

    @Override
    public void successfulPayment() {
        try {
            ArrayList<Account> accountList = new ArrayList<Account>();
            ArrayList<User> userList = new ArrayList<User>();
            ArrayList<Double> amountsList = new ArrayList<Double>();
            for (String iban : getAccountsForSplit()) {
                accountList.add(Bank.getInstance().getAccountByIBAN(iban));
                userList.add(Bank.getInstance().getUserByIBAN(iban));
                amountsList.add(amountsForUsers.get((accountList.size() - 1)) * Bank.getInstance().findExchangeRate(getCurrency(), accountList.getLast().getCurrency()));
            }

            String problemIban = "";
            for (Account account : accountList) {
                if (account.getBalance() - amountsList.get(accountList.indexOf(account)) < account.getMinBalance()) {
                    problemIban = account.getIban();
                    break;
                }
            }
            if (problemIban.isEmpty()) {
                ObjectNode insertedNode = splitPayment();

                for (int i = 0; i < accountList.size(); i++) {
                    userList.get(i).getTranzactions().add(insertedNode);
                    userList.get(i).getTranzactions().sort(Comparator.comparingInt(t -> t.get("timestamp").asInt()));

                    //userList.get(i).getTranzactions().add(splitPayment());
                    //accountList.get(i).getReportsClassic().add(splitPayment());
                    userList.get(i).checkFivePayments(amountsList.get(i) * Bank.getInstance()
                            .findExchangeRate(accountList.get(i).getCurrency(), "RON"), accountList.get(i).getIban(), getTimestamp());
                    Bank.getInstance().getAccountByIBAN(getAccountsForSplit().get(i))
                            .setBalance(accountList.get(i).getBalance()
                                    - amountsList.get(i));
                }
            } else {
                ObjectNode node = splitPayment();

                node.put("error", "Account " + problemIban
                        + " has insufficient funds for a split payment.");
                for (int i = 0; i < userList.size(); i++) {
                    userList.get(i).getTranzactions().add(node);
                    userList.get(i).getTranzactions().sort(Comparator.comparingInt(t -> t.get("timestamp").asInt()));
                    accountList.get(i).getReportsClassic().add(node);
                }
            }
        } catch (AccountNotFoundException e) {

        }
    }



    private ObjectNode splitPayment() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode successNode = mapper.createObjectNode();
        successNode.put("timestamp", getTimestamp());
        successNode.put("description",
                "Split payment of " + String.format("%.2f", getAmount()) + " " + getCurrency());
        successNode.put("currency", getCurrency());


        ArrayNode amountsNode = mapper.createArrayNode();
        for (Double amount : amountsForUsers) {
            amountsNode.add(amount);
        }
        successNode.set("amountForUsers", amountsNode);


        ArrayNode accountsNode = mapper.createArrayNode();
        for (String iban : getAccountsForSplit()) {
            accountsNode.add(iban);
        }
        successNode.set("involvedAccounts", accountsNode);
        successNode.put("splitPaymentType", getSplitPaymentType());
        return successNode;
    }

    public ObjectNode rejectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rejectNode = mapper.createObjectNode();
        ArrayNode amountForUsersNode = mapper.createArrayNode();
        for (Double amount : amountsForUsers) {
            amountForUsersNode.add(amount);
        }
        rejectNode.set("amountForUsers", amountForUsersNode);
        rejectNode.put("currency", getCurrency());
        rejectNode.put("description", "Split payment of " + String.format("%.2f", getAmount()) + " " + getCurrency());
        rejectNode.put("error", "One user rejected the payment.");
        ArrayNode involvedAccountsNode = mapper.createArrayNode();
        for (String iban : getAccountsForSplit()) {
            involvedAccountsNode.add(iban);
        }
        rejectNode.set("involvedAccounts", involvedAccountsNode);
        rejectNode.put("splitPaymentType", getSplitPaymentType());
        rejectNode.put("timestamp", getTimestamp());
        return rejectNode;

    }
}

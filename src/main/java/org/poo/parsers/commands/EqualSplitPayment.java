package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * class implementing the split payment command
 */
public final class EqualSplitPayment implements Command {

    //private final double amount;
    //private final int timestamp;
    //private final String currency;
    //private final List<String> accountsForSplit;
    private CommandInput input;


    public EqualSplitPayment(final CommandInput commandInput) {
        //amount = commandInput.getAmount();
        //timestamp = commandInput.getTimestamp();
        //currency = commandInput.getCurrency();
        //accountsForSplit = commandInput.getAccounts();
        input = commandInput;
    }


    /**
     * all the accounts, ibans and users are added into lists. the method
     * checks if the ibans are correct at first, if everything is okay
     * the accounts have transactions added and balances decreased
     */
    @Override
    public void execute() {
        Bank.getInstance().getSplitPayments().add(new SplitPaymentInfo(input));
//        try {
//            ArrayList<Account> accountList = new ArrayList<Account>();
//            ArrayList<User> userList = new ArrayList<User>();
//            for (String iban : accountsForSplit) {
//                accountList.add(Bank.getInstance().getAccountByIBAN(iban));
//                userList.add(Bank.getInstance().getUserByIBAN(iban));
//            }
//            String problemIban = "";
//            double eachAmount = amount / accountList.size(); // amount per each
//            for (Account account : accountList) {
//                if (account.getBalance() - eachAmount * Bank.getInstance()
//                        .findExchangeRate(currency, account.getCurrency()) < account.getMinBalance()) {
//                    problemIban = account.getIban();
//                }
//            }
//            if (problemIban.isEmpty()) {
//                for (int i = 0; i < accountList.size(); i++) {
//                    userList.get(i).getTranzactions().add(splitPayment());
//                    accountList.get(i).getReportsClassic().add(splitPayment());
//
//                    Bank.getInstance().getAccountByIBAN(accountsForSplit.get(i))
//                            .setBalance(accountList.get(i).getBalance()
//                                    - eachAmount * Bank.getInstance()
//                                    .findExchangeRate(currency, accountList.get(i).getCurrency()));
//                }
//            } else {
//                ObjectNode node = splitPayment();
//                node.put("error", "Account " + problemIban
//                        + " has insufficient funds for a split payment.");
//                for (int i = 0; i < userList.size(); i++) {
//                    userList.get(i).getTranzactions().add(node);
//                    accountList.get(i).getReportsClassic().add(node);
//                }
//            }
//        } catch (AccountNotFoundException e) {
//
//        }
    }



//    private ObjectNode splitPayment() {
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode successNode = mapper.createObjectNode();
//        successNode.put("timestamp", timestamp);
//        successNode.put("description",
//                "Split payment of " + String.format("%.2f", amount) + " " + currency);
//        successNode.put("currency", currency);
//        successNode.put("amount", amount / accountsForSplit.size());
//        ArrayNode accountsNode = mapper.createArrayNode();
//        for (String iban : accountsForSplit) {
//            accountsNode.add(iban);
//        }
//        successNode.set("involvedAccounts", accountsNode);
//        return successNode;
//    }
}

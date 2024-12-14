package org.poo.visitors.reportVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class implementing visitor interface, visiting 2 types of accounts
 */
public final class SpendingsReportVisitor implements Visitor {
    private String iban;
    private int timestamp;
    private int startTimestamp;
    private int endTimestamp;


    public SpendingsReportVisitor(final String iban, final int timestamp,
                                  final int startTimestamp, final int endTimestamp) {
        this.iban = iban;
        this.timestamp = timestamp;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }


    /**
     * takes classic account tranzactions based on the start and end timestamps
     * creates the commerciants ArrayList by initially using a HashMap
     * sorts the commerciants ArrayList as well
     * @param account classic account
     */
    public void visit(final ClassicAccount account) {
        ObjectMapper mapper = new ObjectMapper();

        List<ObjectNode> tranzactions = Bank.getInstance().getAccountByIBAN(iban)
                .getSpendingReports().stream().filter(node -> {
            int timeStamp = node.get("timestamp").asInt();
            return timeStamp >= startTimestamp && timeStamp <= endTimestamp;
        }).collect(Collectors.toList());


        Map<String, Double> commerciantTotals = new LinkedHashMap<String, Double>();
        if  (tranzactions.size() > 0) {
            for (ObjectNode tranzaction : tranzactions) {

                String commerciant = tranzaction.get("commerciant").asText();
                double amount = tranzaction.get("amount").asDouble();
                commerciantTotals.put(commerciant,
                        commerciantTotals.getOrDefault(commerciant, 0.0) + amount);

            }
        }


        List<ObjectNode> commerciants = new ArrayList<ObjectNode>();
        for (Map.Entry<String, Double> entry : commerciantTotals.entrySet()) {
            ObjectNode commerciantNode = mapper.createObjectNode();
            commerciantNode.put("commerciant", entry.getKey());
            commerciantNode.put("total", entry.getValue());
            commerciants.add(commerciantNode);
        }


        commerciants.sort((a, b) -> a.get("commerciant").asText()
                .compareTo(b.get("commerciant").asText()));


        addToOutput(tranzactions, account, commerciants);
    }



    /**
     * adds an error to the output ArrayNode
     * @param account savings account
     */
    public void visit(final SavingsAccount account) {
        Bank.getInstance().getOutput().add(spendingsReportOnSavingsAccountError());
    }



    private void addToOutput(final List<ObjectNode> tranzactions, final Account account,
                             final List<ObjectNode> commerciants) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "spendingsReport");
        ObjectNode tranzaction = mapper.createObjectNode();
        tranzaction.put("IBAN", iban);
        tranzaction.put("balance", account.getBalance());
        tranzaction.put("currency", account.getCurrency());
        ArrayNode transactions = mapper.createArrayNode();
        if (transactions != null) {
            for (ObjectNode tranzactionNode : tranzactions) {
                transactions.add(tranzactionNode);
            }
        }
        tranzaction.set("transactions", transactions);
        ArrayNode commerciantsNode = mapper.createArrayNode();
        if (commerciants != null) {
            for (ObjectNode commerciantNode : commerciants) {
                commerciantsNode.add(commerciantNode);
            }
        }
        tranzaction.set("commerciants", commerciantsNode);
        node.set("output", tranzaction);
        node.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(node);
    }



    private ObjectNode spendingsReportOnSavingsAccountError() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "spendingsReport");
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("error", "This kind of report is not supported for a saving account");
        node.set("output", errorNode);
        node.put("timestamp", timestamp);
        return node;
    }


}

package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import java.util.*;
import java.util.stream.Collectors;

public class SpendingsReportVisitor {
    private String IBAN;
    private int timestamp;
    private int startTimestamp;
    private int endTimestamp;

    public SpendingsReportVisitor(String IBAN, int timestamp, int startTimestamp, int endTimestamp) {
        this.IBAN = IBAN;
        this.timestamp = timestamp;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public void visit(ClassicAccount account) {


        List<ObjectNode> tranzactions = Bank.getInstance().getAccountByIBAN(IBAN).getSpendingReports().stream().filter(node -> {
            int timestamp = node.get("timestamp").asInt();
            return timestamp >= startTimestamp && timestamp <= endTimestamp;
        }).collect(Collectors.toList());






        Map<String, Double> commerciantTotals = new LinkedHashMap<>();
        if(tranzactions.size() > 0) {
            for (ObjectNode tranzaction : tranzactions) {

                String commerciant = tranzaction.get("commerciant").asText();
                double amount = tranzaction.get("amount").asDouble();
                commerciantTotals.put(commerciant, commerciantTotals.getOrDefault(commerciant, 0.0) + amount);

            }
        }

        ObjectMapper mapper = new ObjectMapper();

        List<ObjectNode> commerciants = new ArrayList<>();
        for (Map.Entry<String, Double> entry : commerciantTotals.entrySet()) {
            ObjectNode commerciantNode = mapper.createObjectNode();
            commerciantNode.put("commerciant", entry.getKey());
            commerciantNode.put("total", entry.getValue());
            commerciants.add(commerciantNode);
        }


        commerciants.sort((a, b) -> a.get("commerciant").asText().compareTo(b.get("commerciant").asText()));


        addToOutput(tranzactions, account, commerciants);
    }

    public void visit(SavingsAccount account) {
        Bank.getInstance().getOutput().add(spendingsReportOnSavingsAccountError());
    }

    public void addToOutput(List<ObjectNode> tranzactions, Account account, List<ObjectNode> commerciants) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "spendingsReport");
        ObjectNode tranzaction = mapper.createObjectNode();
        tranzaction.put("IBAN", IBAN);
        tranzaction.put("balance", account.getBalance());
        tranzaction.put("currency", account.getCurrency());
        ArrayNode transactions = mapper.createArrayNode();
        if(transactions != null) {
            for (ObjectNode tranzactionNode : tranzactions) {
                transactions.add(tranzactionNode);
            }
        }
        tranzaction.set("transactions", transactions);
        ArrayNode commerciantsNode = mapper.createArrayNode();
        if(commerciants != null) {
            for (ObjectNode commerciantNode : commerciants) {
                commerciantsNode.add(commerciantNode);
            }
        }
        tranzaction.set("commerciants", commerciantsNode);
        node.set("output", tranzaction);
        node.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(node);
    }

    public ObjectNode spendingsReportOnSavingsAccountError() {
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

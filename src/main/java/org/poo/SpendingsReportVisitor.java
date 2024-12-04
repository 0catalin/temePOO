package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.baseinput.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpendingsReportVisitor {
    private String IBAN;
    private int timestamp;
    private int startTimestamp;
    private int endTimestamp;
    private User user;
    private ObjectMapper mapper;
    private ArrayNode output;

    public SpendingsReportVisitor(String IBAN, int timestamp, int startTimestamp, int endTimestamp, User user, ObjectMapper mapper, ArrayNode output) {
        this.IBAN = IBAN;
        this.timestamp = timestamp;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.user = user;
        this.mapper = mapper;
        this.output = output;
    }

    public void visit(ClassicAccount account) {
        List<ObjectNode> tranzactions = user.getTranzactions().stream().filter(node -> {
            int timestamp = node.get("timestamp").asInt();
            return timestamp >= startTimestamp && timestamp <= endTimestamp && node.has("commerciant");
        }).collect(Collectors.toList());


        Map<String, Double> commerciantTotals = new HashMap<>();
        for (ObjectNode transaction : tranzactions) {

            String commerciant = transaction.get("commerciant").asText();
            double amount = transaction.get("amount").asDouble();
            commerciantTotals.put(commerciant, commerciantTotals.getOrDefault(commerciant, 0.0) + amount);

        }

        List<ObjectNode> commerciants = new ArrayList<>();
        for (Map.Entry<String, Double> entry : commerciantTotals.entrySet()) {
            ObjectNode commerciantNode = mapper.createObjectNode();
            commerciantNode.put("commerciant", entry.getKey());
            commerciantNode.put("total", entry.getValue());
            commerciants.add(commerciantNode);
        }

        commerciants.sort((a, b) -> Double.compare(b.get("total").asDouble(), a.get("total").asDouble()));



        addToOutput(output, mapper, tranzactions, account, commerciants);
    }

    public void visit(SavingsAccount account) {
        List<ObjectNode> tranzactions = new ArrayList<>();
        List<ObjectNode> commerciants = new ArrayList<>();
        addToOutput(output, mapper, tranzactions, account, commerciants);
    }

    public void addToOutput(ArrayNode output, ObjectMapper mapper, List<ObjectNode> tranzactions, Account account, List<ObjectNode> commerciants) {
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
        output.add(node);
    }
}

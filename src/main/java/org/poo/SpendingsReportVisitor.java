package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;

import java.util.*;
import java.util.stream.Collectors;

public class SpendingsReportVisitor {
    private String IBAN;
    private int timestamp;
    private int startTimestamp;
    private int endTimestamp;
    private User user;
    private ObjectMapper mapper;
    private ArrayNode output;
    private Bank bank;

    public SpendingsReportVisitor(String IBAN, int timestamp, int startTimestamp, int endTimestamp, User user, ObjectMapper mapper, ArrayNode output, Bank bank) {
        this.IBAN = IBAN;
        this.timestamp = timestamp;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.user = user;
        this.mapper = mapper;
        this.output = output;
        this.bank = bank;
    }

    public void visit(ClassicAccount account) {
        List<ObjectNode> tranzactionsFiltered = new ArrayList<>();

        LinkedHashMap<ObjectNode, List<String>> tranzactions = user.getTranzactions().entrySet().stream().filter(entry -> {
            ObjectNode node = entry.getKey();
            int timestamp = node.get("timestamp").asInt();
            return timestamp >= startTimestamp && timestamp <= endTimestamp && node.has("commerciant");
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        if (tranzactions != null) {
            for (Map.Entry<ObjectNode, List<String>> entry : tranzactions.entrySet()) {
                ObjectNode tranzaction = entry.getKey();
                if (tranzactions.get(tranzaction).get(0).equals(IBAN)) {
                    tranzactionsFiltered.add(tranzaction);
                }
            }
        }

        Map<String, Double> commerciantTotals = new LinkedHashMap<>();
        if(tranzactionsFiltered.size() > 0) {
            for (ObjectNode tranzaction : tranzactionsFiltered) {

                String commerciant = tranzaction.get("commerciant").asText();
                double amount = tranzaction.get("amount").asDouble();
                commerciantTotals.put(commerciant, commerciantTotals.getOrDefault(commerciant, 0.0) + amount);

            }
        }

        List<ObjectNode> commerciants = new ArrayList<>();
        for (Map.Entry<String, Double> entry : commerciantTotals.entrySet()) {
            ObjectNode commerciantNode = mapper.createObjectNode();
            commerciantNode.put("commerciant", entry.getKey());
            commerciantNode.put("total", entry.getValue());
            commerciants.add(commerciantNode);
        }


        commerciants.sort((a, b) -> a.get("commerciant").asText().compareTo(b.get("commerciant").asText()));


        addToOutput(output, mapper, tranzactionsFiltered, account, commerciants);
    }

    public void visit(SavingsAccount account) {
    output.add(spendingsReportOnSavingsAccountError(mapper));

        // List<ObjectNode> tranzactions = new ArrayList<>();
        // List<ObjectNode> commerciants = new ArrayList<>();
        // addToOutput(output, mapper, tranzactions, account, commerciants); implementare veche
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

    public ObjectNode spendingsReportOnSavingsAccountError(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "spendingsReport");
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("error", "This kind of report is not supported for a saving account");
        node.set("output", errorNode);
        node.put("timestamp", timestamp);
        return node;
    }


}

package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import java.util.List;
import java.util.stream.Collectors;

public class ReportVisitor {

    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private String IBAN;
    public ReportVisitor(int startTimestamp, int endTimestamp, int timestamp, String IBAN) {

        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.IBAN = IBAN;
    }

    public void visit(ClassicAccount account) {
        List<ObjectNode> tranzactions = Bank.getInstance().getAccountByIBAN(IBAN).getReportsClassic().stream().filter(node -> {
            int timestamp = node.get("timestamp").asInt();
            return timestamp >= startTimestamp && timestamp <= endTimestamp;
        }).collect(Collectors.toList());
        addToOutput(tranzactions, account);
    }


    public void visit(SavingsAccount account) {
        List<ObjectNode> tranzactions = Bank.getInstance().getAccountByIBAN(IBAN).getReportsSavings().stream().filter(node -> {
            int timestamp = node.get("timestamp").asInt();
            return timestamp >= startTimestamp && timestamp <= endTimestamp;
        }).collect(Collectors.toList());

        addToOutput(tranzactions, account);
    }

    private void addToOutput(List<ObjectNode> tranzactions, Account account) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "report");
        ObjectNode tranzaction = mapper.createObjectNode();
        tranzaction.put("IBAN", IBAN);
        tranzaction.put("balance", account.getBalance());
        tranzaction.put("currency", account.getCurrency());
        ArrayNode transactions = mapper.createArrayNode();
        for (ObjectNode tranzactionNode : tranzactions) {
            transactions.add(tranzactionNode);
        }
        tranzaction.set("transactions", transactions);
        node.set("output", tranzaction);
        node.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(node);
    }
}

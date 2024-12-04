package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportVisitor {
    private User user;
    private ObjectMapper mapper;
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private ArrayNode output;
    private String IBAN;
    private Bank bank;
    public ReportVisitor(User user, ObjectMapper mapper, int startTimestamp, int endTimestamp, int timestamp, ArrayNode output, String IBAN, Bank bank) {
        this.user = user;
        this.mapper = mapper;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.output = output;
        this.IBAN = IBAN;
        this.bank = bank;
    }

    public void visit(ClassicAccount account) {
        List<ObjectNode> tranzactionsFiltered = new ArrayList<>();
        List<ObjectNode> tranzactionsFiltered2 = new ArrayList<>();
        List<ObjectNode> tranzactions = user.getTranzactions().stream().filter(node -> {
            int timestamp = node.get("timestamp").asInt();
            return timestamp >= startTimestamp && timestamp <= endTimestamp;
        }).collect(Collectors.toList());

 /*
        if(tranzactions != null) {
            for (int i = 0; i < tranzactions.size(); i++) {
                if(bank.getMap2().containsKey(i) && bank.getMap2().get(i).equals(IBAN)){
                    tranzactionsFiltered.add(tranzactions.get(i));
                } else if (!bank.getMap2().containsKey(i)){
                    tranzactionsFiltered.add(tranzactions.get(i));
                }
            }
        }





        if (tranzactionsFiltered != null) {
            for (ObjectNode tranzaction : tranzactionsFiltered) {

                if (bank.getMap().containsKey(tranzaction.get("timestamp").asInt()) && bank.getMap().get(tranzaction.get("timestamp").asInt()).equals(IBAN)) {
                    tranzactionsFiltered2.add(tranzaction);
                }
            }
        }
*/

        addToOutput(output, mapper, tranzactions, account);
    }


    public void visit(SavingsAccount account) {

    }

    private void addToOutput(ArrayNode output, ObjectMapper mapper, List<ObjectNode> tranzactions, Account account) {
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
        output.add(node);
    }
}

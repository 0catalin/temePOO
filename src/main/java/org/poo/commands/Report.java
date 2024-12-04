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
import java.util.stream.Collectors;

public class Report implements Command{
    private int startTimestamp;
    private int endTimestamp;
    private String IBAN;
    private int timestamp;

    public Report(CommandInput commandInput) {
        IBAN = commandInput.getAccount();
        endTimestamp = commandInput.getEndTimestamp();
        startTimestamp = commandInput.getStartTimestamp();
        timestamp = commandInput.getTimestamp();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByIBAN(IBAN);
        User user = bank.getUserByIBAN(IBAN);
        if(user != null) {
            List<ObjectNode> tranzactions = user.getTranzactions().stream().filter(node -> {
                int timestamp = node.get("timestamp").asInt();
                return timestamp >= startTimestamp && timestamp <= endTimestamp;
            }).collect(Collectors.toList());
            addToOutput(output, mapper, tranzactions, account);
        } else {
            userNotFound(output, mapper);
        }
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

    private void userNotFound(ArrayNode output, ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "report");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", "Account not found");
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}

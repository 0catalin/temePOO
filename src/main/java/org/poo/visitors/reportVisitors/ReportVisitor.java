package org.poo.visitors.reportVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class implementing visitor interface, visiting 2 types of accounts
 */
public final class ReportVisitor implements Visitor {

    private final int startTimestamp;
    private final int endTimestamp;
    private final int timestamp;
    private final String iban;


    public ReportVisitor(final int startTimestamp, final int endTimestamp,
                         final int timestamp, final String iban) {

        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.iban = iban;
    }


    /**
     * only takes classic account tranzactions based on the start and end timestamps
     * @param account classic account
     */
    public void visit(final ClassicAccount account) {
        List<ObjectNode> tranzactions = Bank.getInstance().
                getAccountByIBAN(iban).getReportsClassic().stream().filter(node -> {
            int timeStamp = node.get("timestamp").asInt();
            return timeStamp >= startTimestamp && timeStamp <= endTimestamp;
        }).collect(Collectors.toList());
        addToOutput(tranzactions, account);
    }



    /**
     * only takes savings account tranzactions based on the start and end timestamps
     * @param account savings account
     */
    public void visit(final SavingsAccount account) {
        List<ObjectNode> tranzactions = Bank.getInstance()
                .getAccountByIBAN(iban).getReportsSavings().stream().filter(node -> {
            int timeStamp = node.get("timestamp").asInt();
            return timeStamp >= startTimestamp && timeStamp <= endTimestamp;
        }).collect(Collectors.toList());

        addToOutput(tranzactions, account);
    }



    private void addToOutput(final List<ObjectNode> tranzactions, final Account account) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "report");
        ObjectNode tranzaction = mapper.createObjectNode();
        tranzaction.put("IBAN", iban);
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

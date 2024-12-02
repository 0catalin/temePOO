package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.fileio.CommandInput;

public class PrintTransactions implements Command{
    private String email;
    private int timestamp;
    public PrintTransactions(CommandInput commandInput) {
        email = commandInput.getEmail();
        timestamp = commandInput.getTimestamp();
    }

    @Override
    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        User user = bank.getUserByEmail(email);
        if (user == null) {
            System.out.println("User not found");
        } else {
            ObjectNode transaction = mapper.createObjectNode();
            transaction.put("command", "printTransactions");
            ArrayNode outputNode = mapper.createArrayNode();
            for (ObjectNode objectNode : user.getTranzactions()) {
                outputNode.add(objectNode);
            }
            transaction.set("output", outputNode);
            transaction.put("timestamp", timestamp);
            output.add(transaction);
        }

    }
}

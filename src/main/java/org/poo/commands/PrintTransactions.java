package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.UserNotFoundException;
import org.poo.fileio.CommandInput;

/**
 * class implementing the print transactions command
 */
public final class PrintTransactions implements Command {
    private String email;
    private int timestamp;
    public PrintTransactions(final CommandInput commandInput) {
        email = commandInput.getEmail();
        timestamp = commandInput.getTimestamp();
    }

    @Override
    public void execute() {
        try {
            User user = Bank.getInstance().getUserByEmail(email);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode transaction = mapper.createObjectNode();
            transaction.put("command", "printTransactions");
            ArrayNode outputNode = mapper.createArrayNode();
            for (ObjectNode node : user.getTranzactions()) {
                outputNode.add(node);
            }
            transaction.set("output", outputNode);
            transaction.put("timestamp", timestamp);
            Bank.getInstance().getOutput().add(transaction);
        } catch (UserNotFoundException e) {

        }

    }
}

package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.PaymentInfoNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;

/**
 * class implementing the acceptSplitPayment method
 */
public final class AcceptSplitPayment implements Command {

    private String email;
    private String splitPaymentType;
    private int timestamp;


    public AcceptSplitPayment(final CommandInput commandInput) {
        email = commandInput.getEmail();
        splitPaymentType = commandInput.getSplitPaymentType();
        timestamp = commandInput.getTimestamp();
    }


    @Override
    /**
     * gets the splitPaymentInfo corresponding to the user and updates the observer
     * instance inside it with the email. if the function returns true
     * the payment is removed from the arrayList and all the users are getting notified
     */
    public void execute() {
        try {
            User user = Bank.getInstance().getUserByEmail(email);
            SplitPaymentInfo splitPaymentInfo
                    = Bank.getInstance().getSplitPaymentByTypeAndEmail(email, splitPaymentType);
            if (splitPaymentInfo.getObserver().update(email)) {
                Bank.getInstance().getSplitPayments().remove(splitPaymentInfo);
                splitPaymentInfo.successfulPayment();
            }
        } catch (PaymentInfoNotFoundException ignored) {

        } catch (UserNotFoundException e) {
            Bank.getInstance().getOutput().add(userNotFound());
        }
    }



    private ObjectNode userNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "acceptSplitPayment");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "User not found");
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }

}

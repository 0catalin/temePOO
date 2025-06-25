package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.exceptions.PaymentInfoNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.splitPayment.SplitPaymentInfo;



/**
 * class implementing the reject split payment command
 */
public final class RejectSplitPayment implements Command {

    private final String email;
    private final String splitPaymentType;
    private final int timestamp;



    public RejectSplitPayment(final CommandInput commandInput) {
        email = commandInput.getEmail();
        splitPaymentType = commandInput.getSplitPaymentType();
        timestamp = commandInput.getTimestamp();
    }


    /**
     * method that searches for the split payment, if it exists
     * removes it and adds the failed transaction to all the users
     */
    public void execute() {
        try {
            Bank.getInstance().getUserByEmail(email);
            SplitPaymentInfo splitPaymentInfo = Bank.getInstance()
                    .getSplitPaymentByTypeAndEmail(email, splitPaymentType);
            Bank.getInstance().getSplitPayments().remove(splitPaymentInfo);

            for (String iban : splitPaymentInfo.getAccountsForSplit()) {
                Bank.getInstance().getUserByIBAN(iban).getTranzactions()
                        .add(splitPaymentInfo.rejectNode());
            }
        } catch (PaymentInfoNotFoundException ignored) {

        } catch (UserNotFoundException e) {
            Bank.getInstance().getOutput().add(userNotFound());
        }
    }


    private ObjectNode userNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "rejectSplitPayment");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "User not found");
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        return node;
    }

}

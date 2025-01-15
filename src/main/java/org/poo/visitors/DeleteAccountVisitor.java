package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.visitors.reportVisitors.Visitor;



/**
 * visitor class designed to run different commands on different types of accounts
 */
public final class DeleteAccountVisitor implements Visitor {

    private final int timestamp;
    private final User user;


    public DeleteAccountVisitor(final User user, final int timestamp) {
        this.user = user;
        this.timestamp = timestamp;
    }


    /**
     * if the account and user are found it checks whether it has 0 balance,
     * if it does, it is deleted and if not it is not deleted and it prints error
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {
        if (!account.isEmpty()) {
            deleteFailure();
            Bank.getInstance().getUserByIBAN(account.getIban())
                    .getTranzactions().add(deleteFundsRemaining());
                    account.getReportsClassic().add(deleteFundsRemaining());
            } else if (user.getAccounts().contains(account)) {
                if (!user.getClassicAccounts().contains(account)) {
                    user.getAccounts().remove(account);
                    deleteSuccess();
                } else {
                    user.getClassicAccounts().remove(account);
                    user.getAccounts().remove(account);
                    deleteSuccess();
                }
            } else {
                deleteFailure();
            }
    }


    /**
     * if the user has permissions to delete the account the business
     * account is deleted
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {
        if (!account.getEmailToCards().containsKey(user.getEmail())) {
            return;
        } else if (!account.getRbac().hasPermissions(user.getEmail(), "deleteAccount")) {
            return;
        } else {
            deleteSuccess();
            user.getAccounts().remove(account);
        }
    }


    /**
     * if the account and user are found it checks whether it has 0 balance,
     * if it does, it is deleted and if not it is not deleted and it prints error
     * @param account the classic account
     */
    public void visit(final SavingsAccount account) {
        if (!account.isEmpty()) {
            deleteFailure();
            Bank.getInstance().getUserByIBAN(account.getIban())
                    .getTranzactions().add(deleteFundsRemaining());
            account.getReportsSavings().add(deleteFundsRemaining());
        } else if (user.getAccounts().contains(account)) {
            if (!user.getClassicAccounts().contains(account)) {
                user.getAccounts().remove(account);
                deleteSuccess();
            } else {
                user.getAccounts().remove(account);
                deleteSuccess();
            }
        } else {
            deleteFailure();
        }
    }


    private ObjectNode deleteFundsRemaining() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("description", "Account couldn't be deleted - there are funds remaining");
        finalNode.put("timestamp", timestamp);
        return finalNode;
    }

    private void deleteSuccess() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(finalNode);
    }


    private void deleteFailure() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("error",
                "Account couldn't be deleted - see org.poo.transactions for details");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(finalNode);
    }
}

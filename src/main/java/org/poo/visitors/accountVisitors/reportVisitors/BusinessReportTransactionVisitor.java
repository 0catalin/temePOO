package org.poo.visitors.accountVisitors.reportVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.visitors.accountVisitors.reportVisitors.spendingInfo.SpendingUserInfo;
import org.poo.accounts.business.UserInfo;
import org.poo.accounts.business.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.visitors.accountVisitors.Visitor;

public final class BusinessReportTransactionVisitor implements Visitor {


    private final int startTimestamp;
    private final int endTimestamp;
    private final int timestamp;
    private final String iban;



    public BusinessReportTransactionVisitor(final String iban, final int timestamp,
                                            final int startTimestamp, final int endTimestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.iban = iban;
    }


    /**
     * the method first iterates over all the stored transactions and for each
     * employee and manager it calculates individual and collective sums spent and
     * deposited. then they are all added to an ObjectNode and added to the output
     * @param account the business account
     */
    public void visit(final BusinessAccount account) {
        double totalSpent = 0;
        double totalDeposited = 0;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "businessReport");


        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("IBAN", iban);
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("spending limit", account.getSpendingLimit());
        outputNode.put("deposit limit", account.getDepositLimit());
        outputNode.put("statistics type", "transaction");


        // for each employee the deposited and spent amounts are calculated
        // by iterating through the arrayList of payments
        ArrayNode employeesNode = mapper.createArrayNode();
        for (UserInfo userInfo : account.getEmployees()) {
            double deposited = 0;
            double spent = 0;
            ObjectNode employeeNode = mapper.createObjectNode();
            employeeNode.put("username", userInfo.getUsername());
            for (SpendingUserInfo spendingUserInfo : account.getSpendingUserInfos()) {
                if (spendingUserInfo.getEmail().equals(userInfo.getEmail())
                        && spendingUserInfo.getTimestamp() <= endTimestamp
                        && spendingUserInfo.getTimestamp() >= startTimestamp) {
                    deposited += spendingUserInfo.getDeposited();
                    spent += spendingUserInfo.getSpent();
                    totalDeposited += spendingUserInfo.getDeposited();
                    totalSpent += spendingUserInfo.getSpent();
                }
            }
            employeeNode.put("spent", spent);
            employeeNode.put("deposited", deposited);

            employeesNode.add(employeeNode);
        }

        // for each manager the deposit and spent amounts are calculated
        // by iterating through the arrayList of payments
        ArrayNode managersNode = mapper.createArrayNode();
        for (UserInfo userInfo : account.getManagers()) {
            double deposited = 0;
            double spent = 0;
            ObjectNode managerNode = mapper.createObjectNode();
            managerNode.put("username", userInfo.getUsername());
            for (SpendingUserInfo spendingUserInfo : account.getSpendingUserInfos()) {
                if (spendingUserInfo.getEmail().equals(userInfo.getEmail())) {
                    deposited += spendingUserInfo.getDeposited();
                    spent += spendingUserInfo.getSpent();
                    totalDeposited += spendingUserInfo.getDeposited();
                    totalSpent += spendingUserInfo.getSpent();
                }
            }
            managerNode.put("spent", spent);
            managerNode.put("deposited", deposited);

            managersNode.add(managerNode);
        }


        outputNode.set("managers", managersNode);
        outputNode.set("employees", employeesNode);



        outputNode.put("total spent", totalSpent);
        outputNode.put("total deposited", totalDeposited);


        commandObject.set("output", outputNode);
        commandObject.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(commandObject);
    }



    /**
     * doesn't do anything for a classic account
     * @param account the classic account
     */
    public void visit(final ClassicAccount account) {

    }

    /**
     * doesn't do anything for a savings account
     * @param account the savings account
     */
    public void visit(final SavingsAccount account) {

    }
}

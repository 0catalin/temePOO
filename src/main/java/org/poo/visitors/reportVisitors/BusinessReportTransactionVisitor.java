package org.poo.visitors.reportVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.SpendingUserInfo;
import org.poo.UserInfo;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;

public class BusinessReportTransactionVisitor implements Visitor {
    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private String iban;

    public BusinessReportTransactionVisitor(String iban, int timestamp, int startTimestamp, int endTimestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.iban = iban;
    }

    public void visit(BusinessAccount account) {
        double totalSpent = 0;
        double totalDeposited = 0;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandObject = mapper.createObjectNode();
        commandObject.put("command", "businessReport");


        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("IBAN", iban);
        outputNode.put("balance", (double) Math.round(account.getBalance()));
        outputNode.put("currency", account.getCurrency());
        outputNode.put("spending limit", account.getSpendingLimit());
        outputNode.put("deposit limit", Math.round(account.getDepositLimit() * 100) / 100.0);
        outputNode.put("statistics type", "transaction");



        ArrayNode employeesNode = mapper.createArrayNode();
        for (UserInfo userInfo : account.getEmployees()) {
            double deposited = 0;
            double spent = 0;
            ObjectNode employeeNode = mapper.createObjectNode();
            employeeNode.put("username", userInfo.getUsername());
            for (SpendingUserInfo spendingUserInfo : account.getSpendingUserInfos()) {
                if (spendingUserInfo.getEmail().equals(userInfo.getEmail()) && spendingUserInfo.getTimestamp() <= endTimestamp && spendingUserInfo.getTimestamp() >= startTimestamp) {
                    deposited += spendingUserInfo.getDeposited();
                    spent += spendingUserInfo.getSpent();
                    totalDeposited += spendingUserInfo.getDeposited();
                    totalSpent += spendingUserInfo.getSpent();
                }
            }
            employeeNode.put("spent", (double) Math.round(spent));
            employeeNode.put("deposited", (double) Math.round(deposited));

            employeesNode.add(employeeNode);
        }

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
            managerNode.put("spent", (double) Math.round(spent));
            managerNode.put("deposited", (double) Math.round(deposited));

            managersNode.add(managerNode);
        }


        outputNode.put("managers", managersNode);
        outputNode.put("employees", employeesNode);



        outputNode.put("total spent", (double) Math.round(totalSpent));
        outputNode.put("total deposited", (double) Math.round(totalDeposited));


        commandObject.put("output", outputNode);
        commandObject.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(commandObject);
    }


    public void visit(ClassicAccount account) {

    }


    public void visit(SavingsAccount account) {

    }
}

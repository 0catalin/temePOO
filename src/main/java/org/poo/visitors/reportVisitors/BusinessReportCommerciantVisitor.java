package org.poo.visitors.reportVisitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.SpendingUserInfo;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class BusinessReportCommerciantVisitor implements Visitor {

    private final int startTimestamp;
    private final int endTimestamp;
    private final int timestamp;
    private final String iban;

    public BusinessReportCommerciantVisitor(final int startTimestamp, final int endTimestamp,
                                            final int timestamp, final String iban) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.iban = iban;
    }

    public void visit(final ClassicAccount account) {

    }


    public void visit(final SavingsAccount account) {

    }

    public void visit(final BusinessAccount account) {

        User user = Bank.getInstance().getUserByIBAN(iban);

        List<SpendingUserInfo> spendingUserInfos = new ArrayList<SpendingUserInfo>(
                account.getSpendingUserInfos());
        spendingUserInfos = spendingUserInfos.stream().filter(
                userInfo -> userInfo.getCommerciant() != null
                        && userInfo.getTimestamp() >= startTimestamp
                        && userInfo.getTimestamp() <= endTimestamp
                        && !userInfo.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList());

        Set<String> commerciantSet = spendingUserInfos.stream()
                .map(SpendingUserInfo::getCommerciant).collect(Collectors.toSet());
        List<String> commerciantList = new ArrayList<String>();
        for (String commerciant : commerciantSet) {
            commerciantList.add(commerciant);
        }
        Collections.sort(commerciantList);




        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", "businessReport");

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("IBAN", iban);
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("spending limit", account.getSpendingLimit());
        outputNode.put("deposit limit", account.getDepositLimit());
        outputNode.put("statistics type", "commerciant");


        ArrayNode commerciantArray = mapper.createArrayNode();


        for (String commerciant : commerciantList) {
            ObjectNode oneCommerciantNode = mapper.createObjectNode();
            oneCommerciantNode.put("commerciant", commerciant);


            double totalreceived = 0;
            for (SpendingUserInfo spendingUserInfo : spendingUserInfos) {
                if (spendingUserInfo.getCommerciant().equals(commerciant)) {
                    totalreceived += spendingUserInfo.getSpent();
                }
            }
            oneCommerciantNode.put("total received", totalreceived);

            List<String> emailList = spendingUserInfos.stream().filter(
                    userInfo -> commerciant.equals(userInfo.getCommerciant()))
                    .map(SpendingUserInfo::getEmail).collect(Collectors.toList());
            List<String> managerList = new ArrayList<String>();
            List<String> employeeList = new ArrayList<String>();
            for (String email : emailList) {
                String name = account.getNameByEmail(email);
                if (account.isUserEmployee(email)) {
                    employeeList.add(name);
                } else if (account.isUserManager(email)) {
                    managerList.add(name);
                }
            }
            Collections.sort(managerList);
            Collections.sort(employeeList);
            ArrayNode managerArray = mapper.createArrayNode();
            for (String manager : managerList) {
                managerArray.add(manager);
            }
            ArrayNode employeeArray = mapper.createArrayNode();
            for (String employee : employeeList) {
                employeeArray.add(employee);
            }
            oneCommerciantNode.set("managers", managerArray);
            oneCommerciantNode.set("employees", employeeArray);
            commerciantArray.add(oneCommerciantNode);
        }
        outputNode.set("commerciants", commerciantArray);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(node);
    }
}

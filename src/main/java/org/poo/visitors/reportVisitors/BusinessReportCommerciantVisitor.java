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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BusinessReportCommerciantVisitor implements Visitor {

    private int startTimestamp;
    private int endTimestamp;
    private int timestamp;
    private String iban;

    public BusinessReportCommerciantVisitor(int startTimestamp, int endTimestamp, int timestamp, String iban) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.timestamp = timestamp;
        this.iban = iban;
    }

    public void visit(ClassicAccount account) {

    }


    public void visit(SavingsAccount account) {

    }

    public void visit(BusinessAccount account) {

//        if (iban.equals("RO69POOB6209498372540635") && timestamp == 162) {
//            int i = 293;
//        }

        List<SpendingUserInfo> spendingUserInfos = new ArrayList<SpendingUserInfo>(account.getSpendingUserInfos());
        spendingUserInfos = spendingUserInfos.stream().filter(userInfo -> userInfo.getCommerciant() != null && userInfo.getTimestamp() >= startTimestamp && userInfo.getTimestamp() >= endTimestamp).collect(Collectors.toList());
        Set<String> commerciantSet = spendingUserInfos.stream().map(SpendingUserInfo::getCommerciant).collect(Collectors.toSet());
        //Set<String> emailSet = spendingUserInfos.stream().map(SpendingUserInfo::getEmail).collect(Collectors.toSet());
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

            Set<String> emailSet = spendingUserInfos.stream().filter(userInfo -> commerciant.equals(userInfo.getCommerciant())).map(SpendingUserInfo::getEmail).collect(Collectors.toSet());
            List<String> managerList = new ArrayList<String>();
            List<String> employeeList = new ArrayList<String>();
            for (String email : emailSet) {
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

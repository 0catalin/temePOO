package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;

public class UpgradePlan implements Command{
    private String iban;
    private String newType;
    private int timestamp;

    public UpgradePlan(CommandInput commandInput) {
        iban = commandInput.getAccount();
        newType = commandInput.getNewPlanType();
        timestamp = commandInput.getTimestamp();
    }

    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByAccount(account);
            if (user.getServicePlan().equals(newType)) {
                account.getReportsSavings().add(alreadyHasPlan(newType));
                user.getTranzactions().add(alreadyHasPlan(newType));
            } else if (user.getServicePlan().equals("silver") && !newType.equals("gold") || user.getServicePlan().equals("gold")) {

            } else if (newType.equals("student") || newType.equals("standard")) {
                // nu stiu daca e nevoie aici
            } else if (Bank.getInstance().findExchangeRate("RON", account.getCurrency()) * getUpgradeCost(user.getServicePlan(), newType) > account.getBalance() - account.getMinBalance()) {
                user.getTranzactions().add(insufficientFunds());
            } else {
                account.setBalance(account.getBalance() - Bank.getInstance().findExchangeRate("RON", account.getCurrency()) * getUpgradeCost(user.getServicePlan(), newType));
                user.setServicePlan(newType);
                user.getTranzactions().add(addToSendersTranzactions());
                if (account.getType().equals("savings")) {
                    account.getReportsSavings().add(addToSendersTranzactions());
                }

            }
        } catch (AccountNotFoundException e) {
            Bank.getInstance().getOutput().add(accountNotFound());
        }
    }


    private double getUpgradeCost(String initial, String last) {
        if (last.equals("gold") && initial.equals("silver")) {
            return 250;
        }
        if (last.equals("gold")) {
            return 350;
        }
        if (last.equals("silver")) {
            return 100;
        }
        throw new UserNotFoundException("somethings wrong");
    }

    private ObjectNode addToSendersTranzactions() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode output = mapper.createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "Upgrade plan");
        output.put("accountIBAN", iban);
        output.put("newPlanType", newType);
        return output;
    }

    private ObjectNode accountNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "upgradePlan");
        ObjectNode output = mapper.createObjectNode();
        output.put("description", "Account not found");
        output.put("timestamp", timestamp);
        node.set("output", output);
        node.put("timestamp", timestamp);
        return node;
    }

    private ObjectNode insufficientFunds() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("description", "Insufficient funds");
        node.put("timestamp", timestamp);
        return node;
    }

    private ObjectNode alreadyHasPlan(String plan) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("description", "The user already has the " + plan + " plan.");
        node.put("timestamp", timestamp);
        return node;
    }
}

package org.poo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


@Getter
@Setter
public class RoleBasedAccessControl {
    ArrayList<String> typesOfUsers;
    HashMap<String, ArrayList<String>> roleToPermissions;
    HashMap<String, String> emailToRoleMap;
    HashMap<String, Double> emailToSpendingLimitMap;
    HashMap<String, Double> emailToDepositLimitMap;


    public RoleBasedAccessControl(String ownerEmail, double limit) {
        typesOfUsers = new ArrayList<String>();
        typesOfUsers.addAll(Arrays.asList("owner", "manager", "employee"));
        roleToPermissions = new HashMap<String, ArrayList<String>>();
        ArrayList<String> ownerPermissions = new ArrayList<>();
        ArrayList<String> managerPermissions = new ArrayList<>();
        ArrayList<String> employeePermissions = new ArrayList<>();
        ownerPermissions.addAll(Arrays.asList("setAlias", "deleteOwnCard", "addFunds", "cardPayment",
                "bankTransfer", "createCard", "deleteAnyCard",
                "addNewBusinessAssociate", "changeSpendingLimit", "changeDepositLimit", "deleteAccount"));
        managerPermissions.addAll(Arrays.asList("addFunds", "cardPayment",
                "bankTransfer", "createCard", "deleteAnyCard", "deleteOwnCard"));
        employeePermissions.addAll(Arrays.asList("createCard", "deleteOwnCard",
                "cardPayment", "bankTransfer"));
        roleToPermissions.put("owner", ownerPermissions);
        roleToPermissions.put("manager", managerPermissions);
        roleToPermissions.put("employee", employeePermissions);
        emailToRoleMap = new HashMap<String, String>();
        emailToRoleMap.put(ownerEmail, "owner");

        emailToDepositLimitMap = new HashMap<String, Double>();
        emailToSpendingLimitMap = new HashMap<String, Double>();

        emailToDepositLimitMap.put(ownerEmail, limit);
        emailToSpendingLimitMap.put(ownerEmail, limit);
    }


    public void addEmail(String email, String typeOfUser, double limit) {
        emailToRoleMap.put(email, typeOfUser);
        emailToDepositLimitMap.put(email, limit);
        emailToSpendingLimitMap.put(email, limit);
    }

    public boolean hasPermissions(String email, String permission) {
        return roleToPermissions.get(emailToRoleMap.get(email)).contains(permission);
        // TODO maybe create and add own exception here
    }
}

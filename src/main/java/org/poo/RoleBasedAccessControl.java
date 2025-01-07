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



    public RoleBasedAccessControl(String ownerEmail) {
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


    }


    public void addEmail(String email, String typeOfUser) {
        emailToRoleMap.put(email, typeOfUser);
    }

    public boolean hasPermissions(String email, String permission) {
        return roleToPermissions.get(emailToRoleMap.get(email)).contains(permission);
        // TODO maybe create and add own exception here
    }
}

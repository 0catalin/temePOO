package org.poo.accounts.business;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


@Getter
@Setter

/**
 * class which keeps track of the user's permissions and role in a business account
 */
public final class RoleBasedAccessControl {

    private final ArrayList<String> typesOfUsers;
    private final HashMap<String, ArrayList<String>> roleToPermissions;
    private final HashMap<String, String> emailToRoleMap;



    public RoleBasedAccessControl(final String ownerEmail) {
        typesOfUsers = new ArrayList<String>();
        typesOfUsers.addAll(Arrays.asList("owner", "manager", "employee"));
        roleToPermissions = new HashMap<String, ArrayList<String>>();


        ArrayList<String> ownerPermissions = new ArrayList<String>();
        ArrayList<String> managerPermissions = new ArrayList<String>();
        ArrayList<String> employeePermissions = new ArrayList<String>();


        ownerPermissions.addAll(Arrays.asList("setAlias", "deleteOwnCard",
                "addFunds", "cardPayment",
                "bankTransfer", "createCard", "deleteAnyCard",
                "addNewBusinessAssociate", "changeSpendingLimit",
                "changeDepositLimit", "deleteAccount"));
        managerPermissions.addAll(Arrays.asList("addFunds", "cardPayment",
                "bankTransfer", "createCard", "deleteAnyCard", "deleteOwnCard"));
        employeePermissions.addAll(Arrays.asList("createCard", "deleteOwnCard",
                "cardPayment", "bankTransfer", "addFunds"));


        roleToPermissions.put("owner", ownerPermissions);
        roleToPermissions.put("manager", managerPermissions);
        roleToPermissions.put("employee", employeePermissions);
        emailToRoleMap = new HashMap<String, String>();
        emailToRoleMap.put(ownerEmail, "owner");


    }



    /**
     * adds email to the map associated with the given type of user
     * @param email the given email
     * @param typeOfUser the role in the business account
     */
    public void addEmail(final String email, final String typeOfUser) {
        emailToRoleMap.put(email, typeOfUser);
    }



    /**
     * checks if a user has permissions to do a certain command
     * @param email the user email
     * @param permission the permission requested
     * @return true if he has permissions and else false
     */
    public boolean hasPermissions(final String email, final String permission) {
        return roleToPermissions.get(emailToRoleMap.get(email)).contains(permission);
    }
}

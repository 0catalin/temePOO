package org.poo.splitPayment;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * interface for the observable Objects (the payments)
 */
public interface Observable {

    /**
     *  all the accounts, ibans and users are added into lists. the method
     *  checks if the ibans are correct at first, if everything is okay
     *  the accounts have transactions added and balances decreased
     */
    void successfulPayment();


    /**
     * method that makes a node
     * @return the rejection payment node
     */
    ObjectNode rejectNode();

}

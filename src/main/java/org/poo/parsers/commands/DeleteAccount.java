package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankPair.Bank;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.UserNotFoundException;
import org.poo.parsers.fileio.CommandInput;
import org.poo.visitors.DeleteAccountVisitor;

/**
 * class implementing the delete account command
 */
public final class DeleteAccount implements Command {

    private final String iban;
    private final String email;
    private final int timestamp;


    public DeleteAccount(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        iban = commandInput.getAccount();
    }



    /**
     * if the account and user are found it checks whether it has 0 balance,
     * if it does, it is deleted and if not it is not deleted and it prints error
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByIBAN(iban);
            User user = Bank.getInstance().getUserByEmail(email);

            DeleteAccountVisitor visitor = new DeleteAccountVisitor(user, timestamp);
            account.accept(visitor);
//            if (!account.isEmpty()) {
//                deleteFailure();
//                Bank.getInstance().getUserByIBAN(account.getIban())
//                        .getTranzactions().add(deleteFundsRemaining());
//                account.getReportsClassic().add(deleteFundsRemaining());
//            } else if (user.getAccounts().contains(account)) {
//                if (!user.getClassicAccounts().contains(account)) {
//                    user.getAccounts().remove(account);
//                    deleteSuccess();
//                } else {
//                    user.getClassicAccounts().remove(account);
//                    user.getAccounts().remove(account);
//                    deleteSuccess();
//                }
//            } else {
//                deleteFailure();
//            }
        } catch (AccountNotFoundException | UserNotFoundException e) {
            deleteFailure();
        }
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

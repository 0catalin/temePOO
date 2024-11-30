package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;
import org.poo.fileio.CommandInput;

public class SetAlias implements Command{
    private String account;
    private String email;
    private String alias;

    public SetAlias(CommandInput commandInput) {
        email = commandInput.getEmail();
        account = commandInput.getAccount();
        alias = commandInput.getAlias();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {

    }
}

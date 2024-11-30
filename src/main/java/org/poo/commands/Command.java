package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.Bank;

public interface Command {
    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper);

}

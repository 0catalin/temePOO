package org.poo.parsers;

import com.fasterxml.jackson.databind.node.ArrayNode;

import org.poo.bankGraph.Bank;
import org.poo.commands.Command;

import java.util.ArrayList;



public abstract class CommandParser {
    public static void parse(ArrayList <Command> commands) {

        for (Command command : commands) {
            command.execute();
        }
    }
}

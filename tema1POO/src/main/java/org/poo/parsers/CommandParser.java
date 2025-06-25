package org.poo.parsers;


import org.poo.parsers.commands.Command;
import java.util.ArrayList;


/**
 * class used to parse thorugh an array of commands
 */
public abstract class CommandParser {
    /**
     * parses through the commands and executes each command
     * @param commands the ArrayList of commands
     */
    public static void parse(final ArrayList<Command> commands) {
        for (Command command : commands) {
            command.execute();
        }
    }
}

package org.poo.parsers;


import org.poo.commands.Command;
import java.util.ArrayList;



public abstract class CommandParser {
    public static void parse(final ArrayList<Command> commands) {
        for (Command command : commands) {
            command.execute();
        }
    }
}

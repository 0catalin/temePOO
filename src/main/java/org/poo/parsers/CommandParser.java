package org.poo.parsers;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.Bank;
import org.poo.commands.Command;

import java.util.ArrayList;

@Getter
@Setter

public final class CommandParser {
    // maybe make this a singleton
    // in the future
    private Bank bank;
    public CommandParser(Bank bank) {
        this.bank = bank;
    }

    public void parse(ArrayList <Command> commands, ArrayNode output) {

        for (Command command : commands) {
            command.execute(bank, output);
        }
    }
}

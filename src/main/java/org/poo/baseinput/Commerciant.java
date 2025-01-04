package org.poo.baseinput;
import lombok.Getter;
import lombok.Setter;
import org.poo.parsers.fileio.CommerciantInput;

import java.util.ArrayList;

@Getter
@Setter

/**
 * class designed to store an input commerciant
 */
public final class Commerciant {
    private String commerciant;
    private int id;
    private String account;
    private String type;
    private String cashbackStrategy;

    public Commerciant(final CommerciantInput commerciantInput) {
        commerciant = commerciantInput.getCommerciant();
        id = commerciantInput.getId();
        account = commerciantInput.getAccount();
        type = commerciantInput.getType();
        cashbackStrategy = commerciantInput.getCashbackStrategy();
    }
}

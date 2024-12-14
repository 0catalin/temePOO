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
    private int id;
    private String description;
    private ArrayList<String> commerciants;

    public Commerciant(final CommerciantInput commerciantInput) {
        id = commerciantInput.getId();
        description = commerciantInput.getDescription();
        commerciants = new ArrayList<String>();
        for (String commerciant : commerciantInput.getCommerciants()) {
            commerciants.add(commerciant);
        }
    }
}

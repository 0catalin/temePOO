package org.poo.baseinput;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

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
        this.id = commerciantInput.getId();
        this.description = commerciantInput.getDescription();
        commerciants = new ArrayList<>();
        for (String commerciant : commerciantInput.getCommerciants()) {
            commerciants.add(commerciant);
        }
    }
}

package org.poo.baseinput;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class Commerciant {
    private int id;
    private String description;
    private ArrayList<String> commerciants;
    public Commerciant(CommerciantInput commerciantInput) {
        this.id = commerciantInput.getId();
        this.description = commerciantInput.getDescription();
        commerciants = new ArrayList<>();
        for(String commerciant : commerciantInput.getCommerciants()) {
            commerciants.add(commerciant);
        }
    }
}

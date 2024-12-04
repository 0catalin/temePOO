package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.SpendingsReportVisitor;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Getter
@Setter

public class ClassicAccount extends Account{
    public ClassicAccount(String currency) {
        setCurrency(currency);
        setBalance(0);
        setCards(new ArrayList<>());
        setAlias("");
        setIBAN(Utils.generateIBAN());
        setType("classic");
    }
    public void accept(SpendingsReportVisitor visitor) {
        visitor.visit(this);
    }
}

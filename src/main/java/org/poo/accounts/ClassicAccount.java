package org.poo.accounts;
import lombok.Getter;
import lombok.Setter;
import org.poo.visitors.reportVisitors.Visitor;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Getter
@Setter

public final class ClassicAccount extends Account {
    public ClassicAccount(final String currency) {
        setCurrency(currency);
        setBalance(0);
        setCards(new ArrayList<>());
        setAlias("");
        setIban(Utils.generateIBAN());
        setType("classic");
        setSpendingReports(new ArrayList<>());
        setReportsSavings(new ArrayList<>());
        setReportsClassic(new ArrayList<>());
    }
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }

}

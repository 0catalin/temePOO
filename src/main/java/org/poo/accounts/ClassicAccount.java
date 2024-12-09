package org.poo.accounts;
import lombok.Getter;
import lombok.Setter;
import org.poo.visitors.reportVisitors.Visitor;

@Getter
@Setter
/**
 * class designed to extend Account class, representing a classic account entity
 */
public final class ClassicAccount extends Account {
    public ClassicAccount(final String currency) {
        super(currency);
        setType("classic");
    }

    /**
     * method of accepting the visitor
     * @param visitor the interface of the possible visitor classes
     */
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }

}

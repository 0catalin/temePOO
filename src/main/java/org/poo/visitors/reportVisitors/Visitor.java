package org.poo.visitors.reportVisitors;

import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;

public interface Visitor {
    void visit(SavingsAccount account);
    void visit(ClassicAccount account);
}

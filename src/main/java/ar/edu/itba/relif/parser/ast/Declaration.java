package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

import java.util.LinkedList;
import java.util.List;

/**
 * A declaration of one or more relation variables
 */
public class Declaration extends Statement {
    private List<String> identifiers;

    /**
     * Constructs a Declaration of new relation variables with the given identifiers
     * @param identifiers the identifiers for the relations
     */
    public Declaration(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public <F, E> Object accept(ReturnVisitor<F, E> visitor) {
        return visitor.visit(this);
    }

    /**
     * @return the identifiers declared in this declaration
     */
    public List<String> getIdentifiers() {
        return identifiers;
    }
}

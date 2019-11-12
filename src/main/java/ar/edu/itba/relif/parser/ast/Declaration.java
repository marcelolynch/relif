package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

import java.util.LinkedList;
import java.util.List;

/**
 * A declaration of one or more relation variables
 * The declaration can be either for arbitrary relations
 * in the algebra or for single atoms
 */
public class Declaration extends Statement {
    private final List<String> identifiers;
    private final boolean isAtomDeclaration;

    /**
     * Constructs a Declaration of new relation variables with the given identifiers
     * @param identifiers the identifiers for the relations
     * @param isAtomDeclaration indicates if this declares atom variables
     */
    public Declaration(List<String> identifiers, boolean isAtomDeclaration) {
        this.identifiers = identifiers;
        this.isAtomDeclaration = isAtomDeclaration;
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

    /**
     * @return {@code true} iff this declaration is declaring atom identifiers
     */
    public boolean isAtomDeclaration() {
        return isAtomDeclaration;
    }
}

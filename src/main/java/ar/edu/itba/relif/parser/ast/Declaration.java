package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

import java.util.LinkedList;
import java.util.List;

public class Declaration extends Statement {
    private List<String> identifiers;
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

    public List<String> getIdentifiers() {
        return identifiers;
    }
}

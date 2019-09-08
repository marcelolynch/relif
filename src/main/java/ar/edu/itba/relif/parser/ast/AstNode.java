package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.ToKodkod;
import ar.edu.itba.relif.parser.visitor.Visitor;

public interface AstNode {
    void accept(Visitor v);
    <F, E> Object accept(ReturnVisitor<F,E> visitor);
}

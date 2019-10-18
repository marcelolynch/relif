package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * A node in Relif's abstract syntax tree.
 */
public interface AstNode {
    /**
     * Accepts a Visitor
     * @param v The visitor to accept
     */
    void accept(Visitor v);

    /**
     * Accepts a ReturnVisitor
     * @param visitor the visitor
     * @return the object returned by the ReturnVisitor
     */
    <F, E> Object accept(ReturnVisitor<F,E> visitor);
}

package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.Visitor;

public interface AstNode {
    void accept(Visitor v);
}

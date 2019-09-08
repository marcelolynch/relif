package ar.edu.itba.relif.parser.visitor;

import ar.edu.itba.relif.parser.ast.NotFormula;
import ar.edu.itba.relif.parser.ast.*;

public interface Visitor {
    void visit(Specification s);
    void visit(Command c);
    void visit(Declaration d);
    void visit(MultiplicityFact f);
    void visit(BinaryFormula bf);
    void visit(Rel rel);
    void visit(BinaryRelationExpr binaryRelationExpr);
    void visit(UnaryRelationExpr unaryRelationExpr);
    void visit(NotFormula notFormula);
    void visit(SetFormula setFormula);
}

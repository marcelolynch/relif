package ar.edu.itba.relif.parser.visitor;

import ar.edu.itba.relif.parser.ast.*;

public interface ReturnVisitor<F,E> {
    F visit(Specification s);
    F visit(Command c);
    F visit(Declaration d);
    F visit(MultiplicityFact f);
    F visit(BinaryFormula bf);
    E visit(Rel rel);
    E visit(BinaryRelationExpr binaryRelationExpr);
    E visit(UnaryRelationExpr unaryRelationExpr);
    F visit(NotFormula notFormula);
    F visit(SetFormula setFormula);
}

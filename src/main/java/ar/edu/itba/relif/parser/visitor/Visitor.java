package ar.edu.itba.relif.parser.visitor;

import ar.edu.itba.relif.parser.ast.NotFormula;
import ar.edu.itba.relif.parser.ast.*;

/**
 * A ReturnVisitor traverses a relif AST, visiting every node.
 */

public interface Visitor {
    /**
     * Visits the given specification.
     **/
    void visit(Specification s);

    /**
     * Visits the given command.
     **/
    void visit(Command c);

    /**
     * Visits the given declaration.
     **/
    void visit(Declaration d);

    /**
     * Visits the given multiplicity fact.
     **/
    void visit(MultiplicityFact f);

    /**
     * Visits the given binary formula
     **/
    void visit(BinaryFormula bf);

    /**
     * Visits the given relation.
     **/
    void visit(Rel rel);

    /**
     * Visits the given binary relation expression.
     **/
    void visit(BinaryRelationExpr binaryRelationExpr);

    /**
     * Visits the given unary relation expression.
     **/
    void visit(UnaryRelationExpr unaryRelationExpr);

    /**
     * Visits the given negation formula.
     **/
    void visit(NotFormula notFormula);

    /**
     * Visits the given set formula.
     **/
    void visit(SetFormula setFormula);
}

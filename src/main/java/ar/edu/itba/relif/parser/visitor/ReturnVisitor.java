package ar.edu.itba.relif.parser.visitor;

import ar.edu.itba.relif.parser.ast.*;

/**
 * A ReturnVisitor traverses a relif AST, visiting every node and returning different values
 * when encountering a subtree that represents a Formula (returning F) and
 * a subtree that represents a relation expression (returning E).
 */
public interface ReturnVisitor<F,E> {

    /**
     * Visits the given specification.
     *
     * @return the result of visiting <code>s</code>,
     * which is the formula to solve for the problem given by the specification
     **/
    F visit(Specification s);

    /**
     * Visits the given command.
     *
     * @return the result of visiting <code>c</code>,
     **/
    F visit(Command c);

    /**
     * Visits the given declaration.
     *
     * @return the result of visiting <code>d</code>,
     **/
    F visit(Declaration d);

    /**
     * Visits the given multiplicity fact.
     *
     * @return the result of visiting <code>f</code>,
     **/
    F visit(MultiplicityFact f);

    /**
     * Visits the given binary formula.
     *
     * @return the result of visiting <code>bf</code>,
     **/
    F visit(BinaryFormula bf);

    /**
     * Visits the given relation.
     *
     * @return the result of visiting <code>rel</code>,
     **/
    E visit(Rel rel);

    /**
     * Visits the given binary relation expression.
     *
     * @return the result of visiting <code>binaryRelationExpr</code>,
     **/
    E visit(BinaryRelationExpr binaryRelationExpr);

    /**
     * Visits the given unary relation expression.
     *
     * @return the result of visiting <code>unaryRelationExpr</code>,
     **/
    E visit(UnaryRelationExpr unaryRelationExpr);

    /**
     * Visits the given unary relation expression.
     *
     * @return the result of visiting <code>notFormula</code>,
     **/
    F visit(NotFormula notFormula);

    /**
     * Visits the given set formula.
     *
     * @return the result of visiting <code>setFormula</code>,
     **/
    F visit(SetFormula setFormula);
}

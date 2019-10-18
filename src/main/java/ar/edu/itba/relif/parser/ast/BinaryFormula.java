package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.BinaryLogicalOp;
import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * A formula consisting of two formulas joined by a binary connective
 */
public class BinaryFormula extends Formula {

    private Formula left;
    private Formula right;
    private BinaryLogicalOp operator;

    /**
     * Constructs a binary formula
     * @param left The formula at the left of the connective
     * @param operator The binary logical connective
     * @param right The formula at the right of the connective
     */
    public BinaryFormula(Formula left, BinaryLogicalOp operator, Formula right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    /**
     * @return The left side of the formula
     */
    public Formula getLeft() {
        return left;
    }

    /**
     * @return The right side of the formula
     */
    public Formula getRight() {
        return right;
    }

    public BinaryLogicalOp getOperator() {
        return operator;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public <F, E> Object accept(ReturnVisitor<F, E> visitor) {
        return visitor.visit(this);
    }
}

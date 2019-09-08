package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.BinaryLogicalOp;
import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

public class BinaryFormula extends Formula {

    private Formula left;
    private Formula right;
    private BinaryLogicalOp operator;

    public BinaryFormula(Formula left, BinaryLogicalOp operator, Formula right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Formula getLeft() {
        return left;
    }

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

package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.BinaryRelationOp;
import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * A {@link RelationExpression} consisting of two RelationExpression
 * joined by a binary operator
 */
public class BinaryRelationExpr extends RelationExpression {
    private BinaryRelationOp operator;
    private RelationExpression left;
    private RelationExpression right;

    /**
     * Constructs a binary relation expression
     * @param left The relation expression at the left of the operator
     * @param operator The binary relation operator
     * @param right The relation expression at the right of the connective
     */
    public BinaryRelationExpr(RelationExpression left, BinaryRelationOp operator, RelationExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    /**
     * @return - The binary operator in this expression
     */
    public BinaryRelationOp getOperator() {
        return operator;
    }

    /**
     * @return - the left operand of this expression
     */
    public RelationExpression getLeft() {
        return left;
    }

    /**
     * @return - the right operand of this expression
     */
    public RelationExpression getRight() {
        return right;
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



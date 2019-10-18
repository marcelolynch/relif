package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.SetOperator;
import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * A formula encoding a "set comparison" (equality or inclusion) between
 * relation expression
 *
 * @see RelationExpression
 * @see SetOperator
 */
public class SetFormula extends Formula {
    private SetOperator operator;
    private RelationExpression left;
    private RelationExpression right;

    /**
     * Constructs a SetFormula
     * @param left a relation expression at the left of the operator
     * @param operator the set-comparison operator
     * @param right a relation expression at the right of the operator
     */
    public SetFormula(RelationExpression left, SetOperator operator, RelationExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    /**
     * @return the set-comparison operator of this formula
     */
    public SetOperator getOperator() {
        return operator;
    }

    /**
     * @return the relation expression at the left of the operator
     */
    public RelationExpression getLeft() {
        return left;
    }

    /**
     * @return the relation expression at the right of the operator
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

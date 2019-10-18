package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.UnaryRelationOp;
import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * A {@link RelationExpression} consisting of a unary operator applied to
 * another RelationExpression
 */
public class UnaryRelationExpr extends RelationExpression {
    private UnaryRelationOp operator;
    private RelationExpression operand;

    /**
     * Constructs a unary relation expression
     * @param operator the unary operator applied
     * @param operand the relation expression that the operator acts upon
     */
    public UnaryRelationExpr(UnaryRelationOp operator, RelationExpression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    /**
     * @return the unary operator associated with this expression
     */
    public UnaryRelationOp getOperator() {
        return operator;
    }

    /**
     * @return the relation expression that the unary operator
     */
    public RelationExpression getOperand() {
        return operand;
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

package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.UnaryRelationOp;
import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

public class UnaryRelationExpr extends RelationExpression {
    private UnaryRelationOp operator;
    private RelationExpression operand;

    public UnaryRelationExpr(UnaryRelationOp operator, RelationExpression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public UnaryRelationOp getOperator() {
        return operator;
    }

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

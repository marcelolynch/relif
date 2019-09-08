package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.SetOperator;
import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

public class SetFormula extends Formula {
    private SetOperator operator;
    private RelationExpression left;
    private RelationExpression right;

    public SetFormula(RelationExpression left, SetOperator operator, RelationExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public SetOperator getOperator() {
        return operator;
    }

    public RelationExpression getLeft() {
        return left;
    }

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

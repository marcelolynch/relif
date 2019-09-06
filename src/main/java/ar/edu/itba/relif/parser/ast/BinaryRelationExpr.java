package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.ast.operator.BinaryRelationOp;
import ar.edu.itba.relif.parser.visitor.Visitor;

public class BinaryRelationExpr extends RelationExpression {
    private BinaryRelationOp operator;
    private RelationExpression left;
    private RelationExpression right;

    public BinaryRelationExpr(RelationExpression left, BinaryRelationOp operator, RelationExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public BinaryRelationOp getOperator() {
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
}



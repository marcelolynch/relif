package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.Visitor;

public class NotFormula extends Formula {
    private Formula operand;

    public NotFormula(Formula operand) {
        this.operand = operand;
    }

    public Formula getOperand() {
        return operand;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

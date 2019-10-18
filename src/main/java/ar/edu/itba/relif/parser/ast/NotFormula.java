package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * A negation of a {@link Formula}
 */
public class NotFormula extends Formula {
    private Formula operand;

    /**
     * Constructs a NotFormula that negates the given formula
     * @param operand the formula that the NotFormula will negate
     */
    public NotFormula(Formula operand) {
        this.operand = operand;
    }

    /**
     * @return The formula being negated
     */
    public Formula getOperand() {
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

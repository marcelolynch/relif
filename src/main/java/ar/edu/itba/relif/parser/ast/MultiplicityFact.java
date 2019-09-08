package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

public class MultiplicityFact extends Formula {
    public enum Multiplicity {
        SOME("some"), NO("no");

        private String keyword;

        Multiplicity(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public String toString() {
            return keyword;
        }
    }

    private Multiplicity multiplicity;
    private RelationExpression relationExpression;

    public MultiplicityFact(Multiplicity multiplicity, RelationExpression relationExpression) {
        this.multiplicity = multiplicity;
        this.relationExpression = relationExpression;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public RelationExpression getRelationExpression() {
        return relationExpression;
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

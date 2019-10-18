package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * A formula that represents multiplicity facts, this is, the existence or non-existence of
 * elements in a relation given by a {@link RelationExpression}.
 */
public class MultiplicityFact extends Formula {

    /**
     * The multiplicity constraints.
     * We can express existence (some) or non-existence (no).
     */
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

    /**
     * Constructs a multiplicity fact
     * @param multiplicity the multiplicity associated with this fact
     * @param relationExpression the relation which we are talking about, encoded in a relation expression
     */
    public MultiplicityFact(Multiplicity multiplicity, RelationExpression relationExpression) {
        this.multiplicity = multiplicity;
        this.relationExpression = relationExpression;
    }

    /**
     * @return the multiplicity associated with this fact
     */
    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    /**
     * @return  the relation expression affected by this fact
     */
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

package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

/**
 * Represents a relation given by a previously declared variable
 */
public class Rel extends RelationExpression {

    /**
     * The universal relation (typically noted 1 in relation algebra),
     * which is the upper bound of all the atoms
     */
    public static final Rel UNIV = new Rel("univ");

    /**
     * The identity relation (typically noted 1' in relation algebra),
     * containing all the identity atoms
     */
    public static final Rel IDEN = new Rel("iden");

    private String name;

    /**
     * Constructs an instance of the Relation
     * @param name the name of the relation given by the identifier of the asocciated variable
     */
    public Rel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Rel{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public <F, E> Object accept(ReturnVisitor<F, E> visitor) {
        return visitor.visit(this);
    }

    /**
     * @return the name of this relation
     */
    public String getName() {
        return name;
    }
}

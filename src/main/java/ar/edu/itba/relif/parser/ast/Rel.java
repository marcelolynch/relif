package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

public class Rel extends RelationExpression {

    public static final Rel UNIV = new Rel("univ");
    public static final Rel IDEN = new Rel("iden");

    private String name;

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

    public String getName() {
        return name;
    }
}

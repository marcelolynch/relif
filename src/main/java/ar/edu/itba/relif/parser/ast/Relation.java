package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.Visitor;

public class Relation extends RelationExpression {

    public static final Relation UNIV = new Relation("univ");
    public static final Relation IDEN = new Relation("iden");

    private String name;

    public Relation(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public String getName() {
        return name;
    }
}

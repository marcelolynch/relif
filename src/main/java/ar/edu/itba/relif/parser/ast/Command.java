package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

public class Command implements AstNode {
    private final Scope scope;

    String command;

    public Command(String command, Scope scope) {
        this.command = command;
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                '}';
    }

    public Scope getScope() {
        return scope;
    }

    public String getCommand() {
        return command;
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

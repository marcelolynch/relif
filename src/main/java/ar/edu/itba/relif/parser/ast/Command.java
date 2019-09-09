package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

import java.util.Optional;

public class Command implements AstNode {
    private final Scope scope;

    private CommandType type;
    private Optional<Statement> statement;

    public Command(CommandType type, Optional<Statement> statement, Scope scope) {
        this.statement = statement;
        this.type = type;
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Command{" +
                "type='" + type + '\'' +
                '}';
    }

    public Scope getScope() {
        return scope;
    }

    public CommandType getType() {
        return type;
    }

    public Optional<Statement> getStatement() {
        return statement;
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

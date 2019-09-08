package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

import java.util.List;

public class Specification implements AstNode {
    private List<Statement> statements;
    private Command command;

    public Specification(List<Statement> statements, Command command) {
        this.statements = statements;
        this.command = command;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Command getCommand() {
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

package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

import java.util.List;

/**
 * A Relif Specification. This is the root of the AST, and englobes
 * the list of statements in the specification together with the command
 * that must run.
 */
public class Specification implements AstNode {
    private List<Statement> statements;
    private Command command;

    /**
     * Constructs a specification with a list of statements and a command
     * @param statements the statements on the specification
     * @param command the command associated with the specification
     */
    public Specification(List<Statement> statements, Command command) {
        this.statements = statements;
        this.command = command;
    }

    /**
     * @return the statements on this specification
     */
    public List<Statement> getStatements() {
        return statements;
    }

    /**
     * @return the command associated with the specification
     */
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

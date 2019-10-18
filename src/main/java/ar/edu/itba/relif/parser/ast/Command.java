package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.ReturnVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;

import java.util.Optional;

/**
 * A command to run.
 * The command has a {@link CommandType}, an optional {@link Statement} associated with the command
 * and a {@link Scope} which will set the bounds on the amount of atoms in the universe.
 *
 * @see CommandType
 * @see Scope
 */
public class Command implements AstNode {
    private final Scope scope;

    private CommandType type;
    private Optional<Statement> statement;

    /**
     * Builds a Command node with the given type, statement and scope
     * @param type the type of the Command
     * @param statement an Optional statement associated with the command
     * @param scope the scope of the different kinds of atoms
     */
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

    /**
     * @return the scope of the different kinds of atoms
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * @return the type of this command
     */
    public CommandType getType() {
        return type;
    }

    /**
     * @return the statement associated with this command, or an empty optional if there is none
     */
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

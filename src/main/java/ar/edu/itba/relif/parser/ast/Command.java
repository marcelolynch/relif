package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.parser.visitor.Visitor;

public class Command implements AstNode {
    String command;

    public Command(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                '}';
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

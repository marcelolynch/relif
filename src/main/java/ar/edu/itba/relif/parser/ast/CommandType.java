package ar.edu.itba.relif.parser.ast;

/**
 * The type of a command
 * The statement associated with the command will be added to the formula if the command type is RUN
 * (trying to look for instances) and its negation if the command type is CHECK
 * (trying to look for counterexamples).
 */
public enum CommandType {
    RUN, CHECK;
}

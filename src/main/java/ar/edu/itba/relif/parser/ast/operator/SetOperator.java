package ar.edu.itba.relif.parser.ast.operator;

/**
 * Set comparison operators
 */
public enum SetOperator {
    IN("in"),
    EQUALS("="),
    NOT_IN("not in"),
    NOT_EQUALS("!=");

    private final String symbol;

    SetOperator(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }
    public String toString() { return symbol; }
}

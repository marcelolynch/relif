package ar.edu.itba.relif.parser.ast.operator;

/**
 * Unary operators for relational expressions
 */
public enum UnaryRelationOp {
    CONVERSE("~"),
    UNARY_MINUS("-");

    private final String symbol;

    UnaryRelationOp(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

    public String toString() { return symbol; }

}

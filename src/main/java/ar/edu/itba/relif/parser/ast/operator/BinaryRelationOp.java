package ar.edu.itba.relif.parser.ast.operator;

/**
 * Binary operations between relational terms
 */
public enum BinaryRelationOp {
    PLUS("+"),
    MINUS("-"),
    INTERSECTION("&"),
    COMPOSE(".");

    private final String symbol;
    BinaryRelationOp(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

    @Override
    public String toString() { return symbol; }
}

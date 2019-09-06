package ar.edu.itba.relif.parser.ast.operator;

public enum UnaryRelationOp {
    CONVERSE("~");

    private final String symbol;

    UnaryRelationOp(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

    public String toString() { return symbol; }

}

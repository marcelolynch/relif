package ar.edu.itba.relif.parser.ast;

public class Scope {
    public static final Scope DEFAULT = new Scope(1, 3,3);
    private int identities, symmetrics, asymmetrics;


    public Scope(int identities, int symmetrics, int asymmetrics) {
        this.identities = identities;
        this.symmetrics = symmetrics;
        this.asymmetrics = asymmetrics;
    }

    public int getIdentities() {
        return identities;
    }

    public int getSymmetrics() {
        return symmetrics;
    }

    public int getAsymmetrics() {
        return asymmetrics;
    }
}

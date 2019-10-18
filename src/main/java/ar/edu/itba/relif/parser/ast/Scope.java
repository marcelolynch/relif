package ar.edu.itba.relif.parser.ast;

/**
 * The scope of a Relif problem.
 * The scope consists of bounds on the amount of identity, symmetric and
 * assymetric atoms that the universal relation will contain.
 *
 * The bounds must be provided separately for each class as the atoms are explicitly
 * categorized in one of them and put as Kodkod upper bounds of these classes before solving.
 */
public class Scope {
    public static final Scope DEFAULT = new Scope(1,3,3);
    private int identities, symmetrics, asymmetrics;


    /**
     * Constructs a Scope
     * @param identities upper bound on the amount of identity atoms
     * @param symmetrics upper bound on the amount of symmetric atoms
     * @param asymmetrics upper bound on the amount of asymmetric atoms
     */
    public Scope(int identities, int symmetrics, int asymmetrics) {
        this.identities = identities;
        this.symmetrics = symmetrics;
        this.asymmetrics = asymmetrics;
    }

    /**
     * @return upper bound on the amount of identity atoms
     */
    public int getIdentities() {
        return identities;
    }

    /**
     * @return upper bound on the amount of symmetric atoms
     */
    public int getSymmetrics() {
        return symmetrics;
    }

    /**
     * @return upper bound on the amount of asymmetric atoms
     */
    public int getAsymmetrics() {
        return asymmetrics;
    }
}

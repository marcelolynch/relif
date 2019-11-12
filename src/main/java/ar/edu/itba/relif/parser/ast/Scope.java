package ar.edu.itba.relif.parser.ast;

import ar.edu.itba.relif.util.Pair;

/**
 * The scope of a Relif problem.
 * The scope consists of bounds on the amount of identity, symmetric and
 * assymetric atoms that the universal relation will contain.
 *
 * The bounds must be provided separately for each class as the atoms are explicitly
 * categorized in one of them and put as Kodkod upper bounds of these classes before solving.
 */
public class Scope {
    public static final Scope DEFAULT = new Scope(Pair.of(1, false), Pair.of(3, false), Pair.of(3, false));

    private Pair<Integer, Boolean> identities, symmetrics, asymmetrics;


    /**
     * Constructs a Scope
     * @param identities upper bound on the amount of identity atoms
     * @param symmetrics upper bound on the amount of symmetric atoms
     * @param asymmetrics upper bound on the amount of asymmetric atoms
     */
    public Scope(Pair<Integer, Boolean> identities, Pair<Integer, Boolean> symmetrics, Pair<Integer, Boolean> asymmetrics) {
        this.identities = identities;
        this.symmetrics = symmetrics;
        this.asymmetrics = asymmetrics;
    }

    /**
     * @return bound on the amount of identity atoms. The first component
     * of the pair specifies the upper bound. The second (boolean) component
     * of the pair is true if the bound is exact, that is, the specified bound
     * is both lower and upper
     */
    public Pair<Integer, Boolean> getIdentities() {
        return identities;
    }

    /**
     * @return bound on the amount of symmetric atoms. The first component
     * of the pair specifies the upper bound. The second (boolean) component
     * of the pair is true if the bound is exact, that is, the specified bound
     * is both lower and upper
     */
    public Pair<Integer, Boolean> getSymmetrics() {
        return symmetrics;
    }

    /**
     * @return bound on the amount of asymmetric atoms. The first component
     * of the pair specifies the upper bound. The second (boolean) component
     * of the pair is true if the bound is exact, that is, the specified bound
     * is both lower and upper
     */
    public Pair<Integer, Boolean> getAsymmetrics() {
        return asymmetrics;
    }
}

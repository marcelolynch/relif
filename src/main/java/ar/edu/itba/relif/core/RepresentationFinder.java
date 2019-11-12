package ar.edu.itba.relif.core;

import ar.edu.itba.relif.core.iterator.RepresentationIterator;
import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.ast.Variable;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.config.Options;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ar.edu.itba.relif.core.FormulaUtilities.apply;

public class RepresentationFinder {

    private final Bounds bounds;
    private final Formula formula;
    private final RelifSolution relifSolution;

    public RepresentationFinder(RelifSolution rs, int bound) {
        relifSolution = rs;
        RelifKodkodSolution solution = rs.getUnderlyingKodkodSolution();

        // Bound the sets
        List<Object> universeElems = new ArrayList<>();
        List<String> concreteSetUpperBound = new ArrayList<>();
        for (int i = 0; i < bound; i++) {
            concreteSetUpperBound.add("X" + i);                    // X will be the set in which we'd like a representation
        }

        universeElems.addAll(concreteSetUpperBound);
        // Add the universe of the relation algebra to this universe
        universeElems.addAll(solution.getUniverseAtomList()); // Add all the relation algebra atoms
        Universe universe = new Universe(universeElems);
        Bounds bounds = new Bounds(universe);

        Relation cycles = solution.getCyclesRelation();
        Relation conv = solution.getConverseRelation();
        Relation at = solution.getAtomsRelation();


        TupleSet allAtomsTupleSet = repurposeTuples(solution.getAtomTuples(), universe);
        bounds.boundExactly(at, allAtomsTupleSet);
        bounds.boundExactly(cycles, repurposeTuples(solution.getCyclesTuples(), universe));
        bounds.boundExactly(conv, repurposeTuples(solution.getConverseTuples(), universe));

        Relation x = Relation.unary("X"); // The concrete set
        TupleSet xTupleSet = universe.factory().setOf(concreteSetUpperBound.toArray());
        bounds.bound(x, xTupleSet); // At most these elements in the set X

        Relation labelling = Relation.ternary("labels");


        // Labelling is a partial function X^2 -> At
        // This is, we are labelling the edges of a complete graph with elements of X as vertices
        Formula labellingIsFuncion = FormulaUtilities.ternaryRelationIsPartialFunction(labelling, x, at);

        // Ensure the labelling corresponds to a representation (Hirsch & Hodgkinson)

        // The first condition requires that the labelling of self-edges is under the identity.
        // We can ensure this leaving (X,X,a) out of the upper bound if a is not an identity atom.
        TupleSet identities = repurposeTuples(solution.getIdentityTuples(), universe);
        bounds.bound(labelling, getLabellingBounds(universe.factory(), xTupleSet, allAtomsTupleSet, identities));

        Formula labellingIsRepresentation = getCorrectLabellingFormula(labelling, x, at, conv, cycles);


        this.bounds = bounds;
        this.formula =  labellingIsFuncion.and(labellingIsRepresentation);
    }

    public Formula getFormula() {
        return formula;
    }

    public Bounds getBounds() {
        return bounds;
    }

    // Returns the upper bound for the labelling function
    // The pairs of the form (x,x) are bounded by the identity atoms
    // The pairs of the form (x,y) with x!=y can't be labelled with identities
    //  which is the same to say that the image is bounded by the diversity atoms
    private static TupleSet getLabellingBounds(TupleFactory factory, TupleSet xs, TupleSet atoms, TupleSet identities) {
        // Generate diversity TupleSet
        TupleSet diversity = atoms.clone();
        diversity.removeAll(identities);

        List<Tuple> bound = new ArrayList<>();
        for (Tuple pair : xs.product(xs)) {  // Labelling is a X^2 -> A relation
            if(pair.atom(0) == pair.atom(1)) {
                // It's a pair (x,x)
                bound.addAll(factory.setOf(pair).product(identities)); // Bound with identity as image
            } else {
                bound.addAll(factory.setOf(pair).product(diversity));  // Bound with diversity
            }
        }

        return factory.setOf(bound);
    }

    // Generate TupleSet of this universe with all the tuples from a TupleSet (which comes
    // potentially from another universe's TupleFactory).
    // This method assumes this universe has all the atoms which make up the tupleSet
    // (otherwise an exception will be thrown eventually)
    private static TupleSet repurposeTuples(TupleSet tupleSet, Universe universe) {
        List<Tuple> repurposed = new ArrayList<>();
        TupleFactory tf = universe.factory();

        tupleSet.forEach(t -> {
            List<Object> tupleAtoms = new ArrayList<>();
            for (int i = 0; i < t.arity(); i++) {
                tupleAtoms.add(t.atom(i));
            }
            repurposed.add(tf.tuple(tupleAtoms));
        });

        return tf.setOf(repurposed);
    }


    private static Formula getCorrectLabellingFormula(Relation labelling, Relation x, Relation at, Relation conv, Relation cycles) {
        Formula nonEmptySet = x.some();
        Formula allAtomsAreAssigned = allAtomsAreAssigned(labelling, x, at);
        Formula converseCorrectness = converseCorrectness(labelling, x, conv);
        Formula consistentTriangles = consistentTriangles(labelling, x, cycles);
        Formula consistentTriplesAreWitnessed = consistentTriplesAreWitnessed (labelling, x, at, cycles);
        return nonEmptySet
                .and(allAtomsAreAssigned)
                .and(converseCorrectness)
                .and(consistentTriangles)
                .and(consistentTriplesAreWitnessed)
                ;
    }

    private static Formula allAtomsAreAssigned(Relation labelling, Relation x, Relation at) {
        // All atoms are in the image of the labelling
        // TODO: Preguntarle a Marcelo
        return at.in(apply(labelling, x, x));
    }


    private static Formula consistentTriangles(Relation labelling, Relation x, Relation cycles) {
        Variable a = Variable.unary("a");
        Variable b = Variable.unary("b");
        Variable c = Variable.unary("c");

        Expression l_ab = apply(labelling, a, b);
        Expression l_ac = apply(labelling, a, c);
        Expression l_cb = apply(labelling, c, b);
        Expression ac_comp_cb = apply(cycles, l_ac, l_cb); // L(a,c);L(c,b)


        // If L(a,c) and L(c,b) exist, then L(a,b) exists and
        // L(a,b) <= L(a,c);L(c,b)
        return (l_ac.some().and(l_cb.some())).implies(l_ab.some().and(l_ab.in(ac_comp_cb)))
                .forAll(a.oneOf(x)
                        .and(b.oneOf(x)
                        .and(c.oneOf(x))));
    }

    private static Formula converseCorrectness(Relation labelling, Relation x, Relation conv) {
        Variable a = Variable.unary("a");
        Variable b = Variable.unary("b");

        // L(a,b) = Conv[L(b,a)]
        return apply(labelling, a,b)
                .eq(apply(conv, apply(labelling, b, a)))
                .forAll(a.oneOf(x).and(b.oneOf(x)));
    }


    private static Formula consistentTriplesAreWitnessed(Relation labelling, Relation X, Relation at, Relation cycles) {
        Variable x = Variable.unary("x");
        Variable y = Variable.unary("y");
        Variable w = Variable.unary("w");
        Variable a = Variable.unary("a");
        Variable b = Variable.unary("b");

        Expression l_xy = apply(labelling, x, y);
        Expression l_xw = apply(labelling, x, w);
        Expression l_wy = apply(labelling, w, y);
        Expression a_comp_b = apply(cycles, a, b);

        Formula p = l_xy.some().and(l_xy.in(a_comp_b));
        Formula q = (l_xw.eq(a)).and(l_wy.eq(b)).forSome(w.oneOf(X));

        // L(x,y) <= a;b   -> there is some w s.t L(x,w) = a , L(w,y) = b
        return p.implies(q)
                .forAll(x.oneOf(X)
                        .and(y.oneOf(X))
                        .and(a.oneOf(at))
                        .and(b.oneOf(at)));
    }


    public Iterator<Representation> findAll() {
        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(1);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(50);
        solver.options().setSkolemDepth(0);


        Iterator<Solution> sol = solver.solveAll(getFormula(), getBounds());

        return new RepresentationIterator(relifSolution, sol);
    }

}

package ar.edu.itba.relif.parser.visitor;


import ar.edu.itba.relif.parser.ast.Scope;
import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.ast.Variable;
import kodkod.instance.Bounds;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RelifInstance {
    private final Bounds bounds;
    private final List<String> identityAtoms;
    private final List<String> asymmetricAtoms;
    private final List<String> symmetricAtoms;
    private final List<String> allAtoms;

    private kodkod.ast.Relation identity;
    private kodkod.ast.Relation symmetric;
    private kodkod.ast.Relation asymmetric;
    private kodkod.ast.Relation atoms;

    private final kodkod.ast.Relation cycles;
    private final kodkod.ast.Relation converse;


    private final kodkod.ast.Formula requirements;
    private final TupleSet allAtomsTupleSet;

    public RelifInstance(Scope scope) {
        int ids = scope.getIdentities();
        int syms = scope.getSymmetrics();
        int asyms = scope.getAsymmetrics();

        asyms = 2 * (asyms/2); // Asymmetric atoms are even
        identityAtoms = IntStream.range(0, ids).mapToObj(i -> "I" + i).collect(Collectors.toList());
        asymmetricAtoms = IntStream.range(0, asyms).mapToObj(i -> "A" + i).collect(Collectors.toList());
        symmetricAtoms = IntStream.range(0, syms).mapToObj(i -> "S" + i).collect(Collectors.toList());
        allAtoms = new ArrayList<>();
        allAtoms.addAll(identityAtoms);
        allAtoms.addAll(asymmetricAtoms);
        allAtoms.addAll(symmetricAtoms);

        identity = kodkod.ast.Relation.unary("Ids");
        symmetric = kodkod.ast.Relation.unary("Sym");
        asymmetric = kodkod.ast.Relation.unary("Asym");
        atoms = kodkod.ast.Relation.unary("At");


        // Bound the sets
        Universe universe = new Universe(allAtoms);
        bounds = new Bounds(universe);
        TupleFactory factory = universe.factory();
        allAtomsTupleSet = factory.setOf(allAtoms.toArray());


        bounds.bound(identity, factory.setOf(identityAtoms.toArray()));
        bounds.bound(symmetric, factory.setOf(symmetricAtoms.toArray()));
        bounds.bound(asymmetric, factory.setOf(asymmetricAtoms.toArray()));
        bounds.bound(atoms, allAtomsTupleSet);



        // Converse and cycles
        cycles = kodkod.ast.Relation.nary("cycles", 3);
        converse = kodkod.ast.Relation.nary("conv", 2);

        // Bound the converse relation
        bounds.bound(converse, converseBounds(factory));

        // Bound cycles
        bounds.bound(cycles, cyclesBounds(factory));

        // Build the formula that ensures we're modelling relation algebras
        requirements = axiom1()
                        .and(axiom2())
                        .and(axiom3())
                        .and(axiom4())
                        .and(universeRequirements())
                        .and(converseRequirements())
                        .and(cycleRequirements());

    }

    private TupleSet cyclesBounds(TupleFactory factory) {
        // TODO: At x At x At is a bad upper bound
        return allAtomsTupleSet.product(allAtomsTupleSet).product(allAtomsTupleSet);
    }


    private TupleSet converseBounds(TupleFactory factory) {
        TupleSet converse_bound = factory.noneOf(2);

        // Expicitly name the converse of every atom in the upper bound
        // Identities and symmetric are its own converse
        for (String atom: identityAtoms) {
            converse_bound.add(factory.tuple(atom, atom));
        }

        for (String atom: symmetricAtoms) {
            converse_bound.add(factory.tuple(atom, atom));
        }

        // The converse of a_i is a_{len/2 + i}
        for (int i = 0; i < (asymmetricAtoms.size() / 2); i++) {
            String a = asymmetricAtoms.get(i);
            String b = asymmetricAtoms.get(i + asymmetricAtoms.size()/2);
            converse_bound.add(factory.tuple(a, b));
            converse_bound.add(factory.tuple(b, a));
        }

        return converse_bound;
    }

    public void boundRelation(Relation newRelation) {
        bounds.bound(newRelation, allAtomsTupleSet);
    }


    private kodkod.ast.Formula cycleRequirements() {
        kodkod.ast.Formula cyclesDefinedForAtoms = cycles.in(atoms.product(atoms).product(atoms));
        return cyclesDefinedForAtoms;
    }

    private kodkod.ast.Formula converseRequirements() {
        Variable x = Variable.unary("x");
        kodkod.ast.Formula converseExists = x.join(converse).one().forAll(x.oneOf(atoms));
        kodkod.ast.Formula converseSymmetric = converse.eq(converse.transpose());
        kodkod.ast.Formula allAtomsInConverse = kodkod.ast.Relation.UNIV.join(converse).eq(atoms);
        return converseExists.and(converseSymmetric).and(allAtomsInConverse);
    }


    private kodkod.ast.Formula universeRequirements() {
        // Sym + Ids + Asym = At
        kodkod.ast.Formula atomPartition = identity.union(symmetric).union(asymmetric).eq(atoms);
        return atomPartition;
    }

    private kodkod.ast.Formula axiom1() {
        // Ax. 1: X = X;1'
        // x = x;1' for all atoms
        // { all x: At | x = cycle[x,Ids] }

        Variable x1 = Variable.unary("x");
        return apply(cycles, x1, identity).eq(x1)
                .forAll(x1.oneOf(atoms));
    }


    private kodkod.ast.Formula axiom2() {
        //  all x,y,z: At | z in cycle[x,y] iff y in cycle[conv[x], z]
        Variable x2 = Variable.unary("xz");
        Variable y2 = Variable.unary("y2");
        Variable z2 = Variable.unary("z2");
        kodkod.ast.Formula f1 = z2.in(apply(cycles, x2, y2));
        kodkod.ast.Formula f2 = y2.in(apply(cycles, x2.join(converse), z2));

        return f1.iff(f2)
                .forAll(x2.oneOf(atoms)
                        .and(y2.oneOf(atoms))
                        .and(z2.oneOf(atoms)));
    }

    private kodkod.ast.Formula axiom3() {
        //  fact { all x,y,z: At | z in cycle[x,y] iff x in cycle[z, conv[y]] }
        Variable x3 = Variable.unary("x3");
        Variable y3 = Variable.unary("y3");
        Variable z3 = Variable.unary("z3");
        kodkod.ast.Formula f3_1 = z3.in(apply(cycles, x3, y3));
        kodkod.ast.Formula f3_2 = x3.in(apply(cycles, z3, y3.join(converse)));

        return f3_1.iff(f3_2)
                .forAll(x3.oneOf(atoms)
                        .and(y3.oneOf(atoms))
                        .and(z3.oneOf(atoms)));
    }

    private kodkod.ast.Formula axiom4() {
        // all v,w,x,y: At |
        //      (some z:At | z in cycle[v,x] and z in cycle[w,y])
        //  iff (some z:At | z in cycle[conv[v],w] and z in cycle[x,conv[y]])
        Variable v = Variable.unary("v");
        Variable w = Variable.unary("w");
        Variable x = Variable.unary("x");
        Variable y = Variable.unary("y");
        Variable z = Variable.unary("z");
        Variable z2 = Variable.unary("z2");
        kodkod.ast.Formula f4_1 = z.in(apply(cycles, v, x)).and(z.in(apply(cycles, w, y))).forSome(z.oneOf(atoms));
        kodkod.ast.Formula f4_2 = z2.in(apply(cycles, v.join(converse), w)).and(z2.in(apply(cycles, x, y.join(converse)))).forSome(z2.oneOf(atoms));

        return f4_1.iff(f4_2)
                .forAll(v.oneOf(atoms)
                        .and(w.oneOf(atoms))
                        .and(x.oneOf(atoms))
                        .and(y.oneOf(atoms)));
    }


    // Returns e[x,y]
    private static Expression apply(Expression e, Expression x, Expression y) {
        return y.join((x.join(e)));
    }


    public Bounds getBounds() {
        return bounds;
    }

    public List<String> getIdentityAtoms() {
        return identityAtoms;
    }

    public List<String> getAsymmetricAtoms() {
        return asymmetricAtoms;
    }

    public List<String> getSymmetricAtoms() {
        return symmetricAtoms;
    }

    public List<String> getAllAtoms() {
        return allAtoms;
    }

    public Relation getIdentity() {
        return identity;
    }

    public Relation getSymmetric() {
        return symmetric;
    }

    public Relation getAsymmetric() {
        return asymmetric;
    }

    public Relation getAtoms() {
        return atoms;
    }

    public Relation getCycles() {
        return cycles;
    }

    public Relation getConverse() {
        return converse;
    }

    public Formula getRequirements() {
        return requirements;
    }

}

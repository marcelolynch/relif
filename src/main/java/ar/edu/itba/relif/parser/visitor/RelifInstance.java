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


        // Set the lower bound with the first identity atom -- Id can't be empty,
        // so we just force some identity atom to be in the relation (i.e., the first one)
        bounds.bound(identity, factory.setOf(identityAtoms.get(0)), factory.setOf(identityAtoms.toArray()));

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

        AxiomProvider axiomProvider = new AxiomProvider2(atoms, cycles, converse, identity);
        // Build the formula that ensures we're modelling relation algebras
        requirements =  axiomProvider.getAxioms()
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

    public kodkod.ast.Formula boundRelation(Relation newRelation) {
        bounds.bound(newRelation, allAtomsTupleSet);
        return newRelation.in(atoms);
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

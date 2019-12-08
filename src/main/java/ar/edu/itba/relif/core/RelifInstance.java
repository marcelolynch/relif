package ar.edu.itba.relif.core;


import ar.edu.itba.relif.parser.ast.Scope;
import ar.edu.itba.relif.util.Pair;
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
        Pair<Integer, Boolean> ids = scope.getIdentities();
        Pair<Integer, Boolean> syms = scope.getSymmetrics();
        Pair<Integer, Boolean> asyms = scope.getAsymmetrics();

        if (!asyms.snd()) {
            // We force the number of asymmetric atoms to be even
            asyms = Pair.of(2 * (asyms.fst() / 2), asyms.snd());
        }
        identityAtoms = IntStream.range(0, ids.fst()).mapToObj(i -> "I" + i).collect(Collectors.toList());
        asymmetricAtoms = IntStream.range(0, asyms.fst()).mapToObj(i -> "A" + i).collect(Collectors.toList());
        symmetricAtoms = IntStream.range(0, syms.fst()).mapToObj(i -> "S" + i).collect(Collectors.toList());
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

        if (identityAtoms.isEmpty()) {
            // With 0 identity atoms the problem will be UNSAT.
            // Still, we don't fail.
            bounds.bound(atoms, allAtomsTupleSet);
        } else {
            // The first identity atom is also on the lower bound for the atoms
            bounds.bound(atoms, factory.setOf(identityAtoms.get(0)), allAtomsTupleSet);
        }

        // The boolean in the second of the pair
        // indicates that the bound is exact
        if (ids.snd()) {
            bounds.boundExactly(identity, factory.setOf(identityAtoms.toArray()));
        } else {
            if (!identityAtoms.isEmpty()) {
                // Set the lower bound with the first identity atom -- Id can't be empty,
                // so we just force some identity atom to be in the relation (i.e., the first one)
                bounds.bound(identity, factory.setOf(identityAtoms.get(0)), factory.setOf(identityAtoms.toArray()));
            } else {
                // With 0 identity atoms the problem will be UNSAT.
                // Again, we don't fail.
                bounds.bound(identity, factory.setOf(identityAtoms.toArray()));
            }
        }

        if (syms.snd()) {
            bounds.boundExactly(symmetric, factory.setOf(symmetricAtoms.toArray()));
        } else {
            bounds.bound(symmetric, factory.setOf(symmetricAtoms.toArray()));
        }

        if (asyms.snd()) {
            bounds.boundExactly(asymmetric, factory.setOf(asymmetricAtoms.toArray()));
        } else {
            bounds.bound(asymmetric, factory.setOf(asymmetricAtoms.toArray()));
        }



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
        kodkod.ast.Formula converseFunctional = converse.function(atoms, atoms);
        kodkod.ast.Formula converseSymmetric = converse.eq(converse.transpose());
        return converseFunctional.and(converseSymmetric);
    }


    private kodkod.ast.Formula universeRequirements() {
        // Sym + Ids + Asym = At
        kodkod.ast.Formula nonEmptyIdentity = identity.some();
        kodkod.ast.Formula atomPartition = identity.union(symmetric).union(asymmetric).eq(atoms);
        return nonEmptyIdentity.and(atomPartition);
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

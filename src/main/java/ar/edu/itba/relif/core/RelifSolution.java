package ar.edu.itba.relif.core;

import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.instance.Instance;
import kodkod.instance.TupleSet;

import java.util.ArrayList;
import java.util.List;

public class RelifSolution {
    private Instance kodkodInstance;

    public RelifSolution(Solution s) {
        kodkodInstance = s.instance().clone();
    }


    private TupleSet getTuples(String name) {
        return kodkodInstance.relations()
                .stream()
                .filter(r -> r.name().equals(name))
                .findFirst()
                .map(t -> kodkodInstance.relationTuples().get(t))
                .get();
    }

    private Relation getRelation(String name) {
        return kodkodInstance.relations()
                .stream()
                .filter(r -> r.name().equals(name))
                .findFirst()
                .get();
    }

    public List<Object> getUniverseAtoms() {
        List<Object> atoms = new ArrayList<>();
        kodkodInstance.universe().iterator().forEachRemaining(atoms::add);
        return atoms;

    }

    public Relation getAtoms() {
        return getRelation("At");
    }

    public TupleSet getAtomTuples() {
        return getTuples("At");
    }

    public Relation getConverse() {
        return getRelation("conv");
    }

    public TupleSet getConverseTuples() {
        return getTuples("conv");
    }

    public Relation getCycles() {
        return getRelation("cycles");
    }

    public TupleSet getCyclesTuples() {
        return getTuples("cycles");
    }


    public TupleSet getIdentityTuples() {
        return getTuples("Ids");
    }
}

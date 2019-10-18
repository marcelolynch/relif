package ar.edu.itba.relif.core;

import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.instance.Instance;
import kodkod.instance.TupleSet;

import java.util.*;
import java.util.stream.Collectors;

public class RelifKodkodSolution {
    private static final Set<String> CORE_RELATIONS = new HashSet<>(Arrays.asList("At", "Ids", "Sym", "Asym", "conv", "cycles"));
    private Instance kodkodInstance;

    public RelifKodkodSolution(Solution s) {
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

    public Map<String, Relation> getUserRelations() {
        return kodkodInstance.relations()
                .stream()
                .filter(r -> !CORE_RELATIONS.contains(r.name()))
                .collect(Collectors.toMap(r -> r.name(), r -> r));
    }

    public Map<String, TupleSet> getUserRelationTupleSets() {
        return kodkodInstance.relations()
                .stream()
                .filter(r -> !CORE_RELATIONS.contains(r.name()))
                .collect(Collectors.toMap(r -> r.name(), r -> kodkodInstance.relationTuples().get(r)));
    }



    public List<Object> getUniverseAtomList() {
        List<Object> atoms = new ArrayList<>();
        kodkodInstance.universe().iterator().forEachRemaining(atoms::add);
        return atoms;
    }

    public Relation getAtomsRelation() {
        return getRelation("At");
    }

    public TupleSet getAtomTuples() {
        return getTuples("At");
    }

    public Relation getConverseRelation() {
        return getRelation("conv");
    }

    public TupleSet getConverseTuples() {
        return getTuples("conv");
    }

    public Relation getCyclesRelation() {
        return getRelation("cycles");
    }

    public TupleSet getCyclesTuples() {
        return getTuples("cycles");
    }


    public TupleSet getIdentityTuples() {
        return getTuples("Ids");
    }
    public TupleSet getSymmetricTuples() {
        return getTuples("Sym");
    }
    public TupleSet getAsymmetricTuples() {
        return getTuples("Asym");
    }
}

package ar.edu.itba.relif.core;


import kodkod.instance.Tuple;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/** Wraps around a RelifKodkod solution
 *  To provide a cleaner representation for the view where atoms are strings
 *  that does not ivolve Kodkod classes
 */
public class RelifSolution {
    private final RelifKodkodSolution rks;

    public RelifSolution(RelifKodkodSolution rks) {
        this.rks = rks;
    }

    public Set<String> getAtoms() {
        return rks.getAtomTuples().stream()
                .map(t -> t.atom(0).toString()).collect(Collectors.toSet());
    }

    public Map<String, String> getConverse() {
        return rks.getConverseTuples().stream()
                .collect(toMap(t -> t.atom(0).toString(), t -> t.atom(1).toString()));
    }

    public Map<String, Map<String, List<String>>> getCycles() {
        Map<String, Map<String, List<String>>> cycles = new HashMap<>();
        for (Tuple t : rks.getCyclesTuples()) {
            String first = t.atom(0).toString();
            String second = t.atom(1).toString();
            String third = t.atom(2).toString();
            cycles.putIfAbsent(first, new HashMap<>());
            cycles.get(first).putIfAbsent(second, new ArrayList<>());
            cycles.get(first).get(second).add(third);
        }

        return cycles;
    }

    /**
     * Get a list of the user declared relations
     */
    public Map<String, List<String>> getUserRelations() {
        return rks.getUserRelationTupleSets().entrySet().stream()
                .collect(toMap(e -> e.getKey(),
                        e -> e.getValue().stream()
                                .map(t -> t.atom(0).toString()).collect(toList())));
    }
}

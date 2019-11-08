package ar.edu.itba.relif.core;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Representation {

    private final Map<String,List<Pair<String,String>>> representationMap;
    private final RelifSolution relifSolution;

    public Representation(RelifSolution relifSolution, Map<String, List<Pair<String, String>>> representationMap) {
        this.representationMap = representationMap;
        this.relifSolution = relifSolution;
    }

    public List<Pair<String, List<Pair<String,String>>>> getAtoms() {
        return representationMap.entrySet()
                .stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public List<Pair<String, List<Pair<String, String>>>> getUserDefinedRelations() {
        List<Pair<String, List<Pair<String,String>>>> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> e: relifSolution.getUserRelations().entrySet()) {
            List<Pair<String, String>> allPairsInRelation = new ArrayList<>();
            for (String atomInRelation: e.getValue()) {
                allPairsInRelation.addAll(representationMap.get(atomInRelation));
            }

            result.add(new Pair<>(e.getKey(), allPairsInRelation));
        }

        return result;
    }

    public List<Pair<String, String>> getBackingSet() {
        Set<Pair<String, String>> pairs = new HashSet<>();
        for (List<Pair<String,String>> l: representationMap.values()) {
            pairs.addAll(l);
        }
        return pairs.stream().sorted(Comparator.<Pair<String,String>, String>comparing(Pair::getKey).thenComparing(Pair::getValue)).collect(Collectors.toList());
    }
}

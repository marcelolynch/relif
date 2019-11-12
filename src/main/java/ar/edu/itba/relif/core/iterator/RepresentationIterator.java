package ar.edu.itba.relif.core.iterator;

import ar.edu.itba.relif.core.RelifSolution;
import ar.edu.itba.relif.core.Representation;
import ar.edu.itba.relif.util.Pair;
import kodkod.engine.Solution;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;

import java.util.*;

public class RepresentationIterator implements Iterator<Representation> {
    private final SatisfiableSolutionIterator solutions;
    private final RelifSolution relifSolution;

    public RepresentationIterator(RelifSolution relifSolution, Iterator<Solution> sol) {
        this.relifSolution = relifSolution;
        this.solutions = new SatisfiableSolutionIterator(sol);
    }


    @Override
    public boolean hasNext() {
        return solutions.hasNext();
    }

    @Override
    public Representation next() {
        Solution next = solutions.next();

        Instance instance = next.instance();

        TupleSet labelling = instance.relations()
                .stream()
                .filter(r -> r.name().equals("labels"))
                .findFirst()
                .map(t -> instance.relationTuples().get(t))
                .get();

        Map<String, List<Pair<String, String>>> representationMap = new HashMap<>();

        for(Tuple t: labelling) {
            System.out.println(t);
            String first = (String) t.atom(0);
            String second  = (String) t.atom(1);
            String label = (String) t.atom(2);

            representationMap.putIfAbsent(label, new ArrayList<>());
            representationMap.get(label).add(Pair.of(first, second));
        }


        return new Representation(relifSolution, representationMap);
    }
}

package ar.edu.itba.relif.core.iterator;
import ar.edu.itba.relif.core.RelifKodkodSolution;
import ar.edu.itba.relif.core.RelifSolution;
import kodkod.engine.Solution;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RelifSolutionIterator implements Iterator<RelifSolution> {

    private final SatisfiableSolutionIterator solutions;

    public RelifSolutionIterator(Iterator<kodkod.engine.Solution> kodkodSolutions) {
        this.solutions = new SatisfiableSolutionIterator(kodkodSolutions);
    }

    @Override
    public boolean hasNext() {
        return solutions.hasNext();
    }

    @Override
    public RelifSolution next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }

        // The call to hasNext will have initialized possiblyNext
        return new RelifSolution(new RelifKodkodSolution(solutions.next()));
    }
}

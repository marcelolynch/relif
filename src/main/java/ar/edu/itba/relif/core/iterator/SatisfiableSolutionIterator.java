package ar.edu.itba.relif.core.iterator;

import ar.edu.itba.relif.core.RelifKodkodSolution;
import ar.edu.itba.relif.core.RelifSolution;
import kodkod.engine.Solution;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SatisfiableSolutionIterator implements Iterator<kodkod.engine.Solution> {

    private final Iterator<Solution> allSolutions;

    // The last solution returned by KodKod is UNSAT
    // we don't want to return that one, so we'll peek
    private Solution possiblyNext = null;

    public SatisfiableSolutionIterator(Iterator<Solution> kodkodSolutions) {
        this.allSolutions = kodkodSolutions;
    }

    @Override
    public boolean hasNext() {
        if(allSolutions.hasNext()) {
            if (possiblyNext == null) {
                possiblyNext = allSolutions.next(); // Peek
            }
            return possiblyNext.sat(); // Only want to return it if the result is SAT
        } else {
            return false;
        }
    }

    @Override
    public Solution next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }

        // The call to hasNext will have initialized possiblyNext
        Solution next = possiblyNext;
        possiblyNext = null;
        return next;
    }
}

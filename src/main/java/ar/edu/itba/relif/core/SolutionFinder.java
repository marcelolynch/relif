package ar.edu.itba.relif.core;

import ar.edu.itba.relif.core.iterator.RelifSolutionIterator;
import ar.edu.itba.relif.parser.ast.Specification;
import kodkod.ast.Formula;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.config.Options;
import kodkod.engine.satlab.SATFactory;

import java.util.Iterator;

public class SolutionFinder {

    public static RelifSolutionIterator findSolutions(Specification spec) {
        RelifInstance rif = new RelifInstance(spec.getCommand().getScope());
        ToKodkod visitor = new ToKodkod(rif);
        Formula specification = visitor.visit(spec);

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(1);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(50);
        solver.options().setSkolemDepth(0);
        System.out.println("Solving...");
        System.out.flush();


        Iterator<Solution> sol = solver.solveAll(rif.getRequirements().and(specification), rif.getBounds());
        return new RelifSolutionIterator(sol);
    }

}

package ar.edu.itba.relif.core;

import ar.edu.itba.relif.parser.SpecificationParser;
import ar.edu.itba.relif.parser.ast.Specification;
import kodkod.ast.Formula;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.config.Options;
import kodkod.engine.satlab.SATFactory;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws Exception {
        String order =
                "rel R\n" +
                "iden in R\n" +
                "R & ~R in iden\n" +
                "R.R in R\n" +
                "R + ~R = univ\n" +
                "iden = ((R - iden).~(R - iden) & iden)\n" +
                "iden = (~(R - iden).(R - iden) & iden)\n" +
                "run {} for 1 id, 0 sym, 2 asym";

        String non_dense_order =
                "rel R\n" +
                        "iden in R\n" +
                        "R & ~R in iden\n" +
                        "R.R in R\n" +
                        "R + ~R = univ\n" +
                        "run {} for 1 id, 0 sym, 8 asym";


        String composition = "rel P,Q,R \n" +
                "no P & Q\n" +
                "no Q & R\n" +
                "no P & R\n" +
                "check {(P.Q).R = P.(Q.R)} for 4";

        String identityIsIdentity = "rel R \n" +
                "some iden\n" +
                "check {(R.iden) = R and iden.R = R} for 3";

        String run = "rel R\n" +
                "run {} for 3";

        run(non_dense_order);
    }


    public static void run(String specString) {
        Reader reader = new StringReader(specString);
        Specification spec = null;
        try {
            spec = SpecificationParser.getSpecification(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // solve(spec);
        Solution s = findSolution(spec).get();
        RepresentationFinder rf = new RepresentationFinder(new RelifSolution(new RelifKodkodSolution(s)), 3);
        solve(rf);
    }


    public static void solve(Specification spec) {
        RelifInstance rif = new RelifInstance(spec.getCommand().getScope());
        ToKodkod visitor = new ToKodkod(rif);
        Formula specification = visitor.visit(spec);

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(1);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(50);
        solver.options().setSkolemDepth(0);


        Iterator<Solution> sol = solver.solveAll(rif.getRequirements().and(specification), rif.getBounds());
        while (sol.hasNext()) {
            Solution s = sol.next();
        }
    }


    public static void solve(RepresentationFinder rf) {
        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(1);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(50);
        solver.options().setSkolemDepth(0);


        Iterator<Solution> sol = solver.solveAll(rf.getFormula(), rf.getBounds());
        while (sol.hasNext()) {
            Solution s = sol.next();
        }
    }

    public static Optional<Solution> findSolution(Specification spec) {
        RelifInstance rif = new RelifInstance(spec.getCommand().getScope());
        ToKodkod visitor = new ToKodkod(rif);
        Formula specification = visitor.visit(spec);

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(1);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(50);
        solver.options().setSkolemDepth(0);


        Iterator<Solution> sol = solver.solveAll(rif.getRequirements().and(specification), rif.getBounds());
        if (sol.hasNext()) {
           return Optional.of(sol.next());
        }

        return Optional.empty();
    }

}

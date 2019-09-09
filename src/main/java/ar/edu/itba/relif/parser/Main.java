package ar.edu.itba.relif.parser;

import ar.edu.itba.relif.parser.ast.Command;
import ar.edu.itba.relif.parser.ast.Specification;
import ar.edu.itba.relif.parser.visitor.RelifInstance;
import ar.edu.itba.relif.parser.visitor.ToKodkod;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import kodkod.ast.Formula;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.config.Options;
import kodkod.engine.satlab.SATFactory;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

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
                "run {} for 2 id, 2 sym, 2 asym";


        String composition = "rel P,Q,R \n" +
                "no P & Q\n" +
                "no Q & R\n" +
                "no P & R\n" +
                "check {(P.Q).R = P.(Q.R)} for 4";

        String idenityIsIdentity = "rel R \n" +
                "some iden\n" +
                "check {(R.iden) = R and iden.R = R} for 3";


        Reader reader = new StringReader(order);
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        parser parser = new parser(new Scanner(reader, sf), sf);
        Symbol result = parser.parse();
        Specification spec = (Specification)result.value;
        solve(spec);
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
        System.out.println("Solving...");
        System.out.flush();


        Iterator<Solution> sol = solver.solveAll(rif.getRequirements().and(specification), rif.getBounds());
        for (Iterator<Solution> it = sol; it.hasNext(); ) {
            Solution s = it.next();
            System.out.println(s.toString());

            System.out.println("=================================================");
        }
    }

}

package ar.edu.itba.relif.parser.visitor;

import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.ast.Variable;

public class AxiomProvider2 implements AxiomProvider {


    private final Relation atoms;
    private final Relation cycles;
    private final Relation identity;
    private final Relation converse;

    public AxiomProvider2(Relation atoms, Relation cycles, Relation converse, Relation identity) {
        this.atoms = atoms;
        this.cycles = cycles;
        this.identity = identity;
        this.converse = converse;
    }


    // Returns e[x,y]
    private static Expression apply(Expression e, Expression x, Expression y) {
        return y.join((x.join(e)));
    }

    @Override
    public Formula getAxioms() {
        return axiom1().and(axiom2()).and(axiom3()).and(axiom4());
    }

    private Formula axiom1() {
        // Ax. 1: X = X;1'
        // x = x;1' for all atoms
        // { all x: At | x = cycle[x,Ids] }

        Variable x1 = Variable.unary("x");
        return apply(cycles, x1, identity).eq(x1)
                .forAll(x1.oneOf(atoms));
    }


    private Formula axiom2() {
        //  all x,y,z: At | z in cycle[x,y] iff y in cycle[conv[x], z]
        Variable x2 = Variable.unary("xz");
        Variable y2 = Variable.unary("y2");
        Variable z2 = Variable.unary("z2");
        Formula f1 = z2.in(apply(cycles, x2, y2));
        Formula f2 = y2.in(apply(cycles, x2.join(converse), z2));

        return f1.iff(f2)
                .forAll(x2.oneOf(atoms)
                        .and(y2.oneOf(atoms))
                        .and(z2.oneOf(atoms)));
    }

    private Formula axiom3() {
        //  fact { all x,y,z: At | z in cycle[x,y] iff x in cycle[z, conv[y]] }
        Variable x3 = Variable.unary("x3");
        Variable y3 = Variable.unary("y3");
        Variable z3 = Variable.unary("z3");
        Formula f3_1 = z3.in(apply(cycles, x3, y3));
        Formula f3_2 = x3.in(apply(cycles, z3, y3.join(converse)));

        return f3_1.iff(f3_2)
                .forAll(x3.oneOf(atoms)
                        .and(y3.oneOf(atoms))
                        .and(z3.oneOf(atoms)));
    }

    private Formula axiom4() {
        // all v,w,x,y: At |
        //      (some z:At | z in cycle[v,x] and z in cycle[w,y])
        //  iff (some z:At | z in cycle[conv[v],w] and z in cycle[x,conv[y]])
        Variable x = Variable.unary("x");
        Variable y = Variable.unary("y");
        Variable z = Variable.unary("z");
        Formula f = apply(cycles, apply(cycles, x, y), z).eq(apply(cycles, x, apply(cycles,y,z)));

        return f.forAll(z.oneOf(atoms)
                        .and(x.oneOf(atoms))
                        .and(y.oneOf(atoms)));
    }
}
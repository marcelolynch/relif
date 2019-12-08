package ar.edu.itba.relif.core;

import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.ast.Variable;

/*
   Utility class for building useful KodKod formulas
  */
public class FormulaUtilities {

    /**
     *  Given a ternary relation r, returns a formula expressing
      * that r : dom x dom -> codom is a (total) function
     */
    public static Formula ternaryRelationIsFunctional(Relation r, Relation dom, Relation codom) {
        Variable x1 = Variable.unary("x1");
        Variable x2 = Variable.unary("x2");
        Formula total = apply(r, x1, x2).one().forAll(x1.oneOf(dom).and(x2.oneOf(dom)));
        Formula wellDefined = r.in(dom.product(dom).product(codom));
        return wellDefined.and(total);
    }

    /**
     *  Given a ternary relation r, returns a formula expressing
     *  that r : dom x dom -> codom is a partial function
     */
    public static Formula ternaryRelationIsPartialFunction(Relation r, Relation dom, Relation codom) {
        Variable x1 = Variable.unary("x1");
        Variable x2 = Variable.unary("x2");

        Formula wellDefined = r.in(dom.product(dom).product(codom));

        Formula atMostOneImage = (apply(r, x1, x2).lone())
                .forAll(x1.oneOf(dom))
                .forAll(x2.oneOf(dom));

        return wellDefined.and(atMostOneImage);
    }



    /**
     * Applies f as a function with the given arguments
     * For example, apply(f,x,y) returns f[x,y], this is, y.(x.f)
     */
    public static Expression apply(Expression f, Expression... arguments) {
        Expression result = f;
        for (Expression arg: arguments) {
            result = arg.join(result);
        }
        return result;
    }
}

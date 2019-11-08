package ar.edu.itba.relif.core;

import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.ast.Variable;

// Builds useful KodKod formulas
public class FormulaUtilities {

    // Given a ternary relation r, return a formula expressing
    // that r : dom x dom -> codom is a function
    public static Formula ternaryRelationIsFunctional(Relation r, Relation dom, Relation codom) {
        Variable x1 = Variable.unary("x1");
        Variable x2 = Variable.unary("x2");
        Formula imageExists = apply(r, x1, x2).one().forAll(x1.oneOf(dom).and(x2.oneOf(dom)));
        Formula totality = r.join(codom).eq(dom.product(dom));
        return totality.and(imageExists);
    }

    // Given a ternary relation r, return a formula expressing
    // that r : dom x dom -> codom is a partial function (at most one image)
    public static Formula ternaryRelationIsPartialFunction(Relation r, Relation dom, Relation codom) {
        Variable x1 = Variable.unary("x1");
        Variable x2 = Variable.unary("x2");

        Formula atMostOneImage = (apply(r, x1, x2).one()).or(apply(r, x1, x2).no())
                .forAll(x1.oneOf(dom))
                .forAll(x2.oneOf(dom));

        Formula wellDefinedDomain = r.join(codom).in(dom.product(dom)); // Domain is the relation
        return atMostOneImage.and(wellDefinedDomain);
    }



    // Apply f as a function with arguments
    // For example, apply(f,x,y) returns f[x,y], this is, y.(x.f)
    public static Expression apply(Expression f, Expression... arguments) {
        Expression result = f;
        for (Expression arg: arguments) {
            result = arg.join(result);
        }
        return result;
    }
}

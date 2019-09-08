package ar.edu.itba.relif.parser.visitor;

import ar.edu.itba.relif.parser.ast.*;
import kodkod.ast.Expression;
import kodkod.ast.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToKodkod implements ReturnVisitor<kodkod.ast.Formula, kodkod.ast.Expression> {

    private RelifInstance instance;
    private Map<String, Relation> symbolTable;

    public ToKodkod(RelifInstance instance) {
        this.instance = instance;
        symbolTable = new HashMap<>();
    }


    @Override
    public kodkod.ast.Formula visit(Specification s) {
        List<kodkod.ast.Formula> formulas = new ArrayList<>();
        for (Statement stmnt: s.getStatements()) {
            kodkod.ast.Formula f = (kodkod.ast.Formula)stmnt.accept(this);
            if (f != null) {
                formulas.add(f);
            }
        }
        s.getCommand().accept(this);
        // TODO: Null

        return formulas.stream().reduce((x,y) -> x.and(y)).orElse(kodkod.ast.Formula.TRUE);
    }

    @Override
    public Void visit(Command c) {
        System.out.println("Command: " + c);
        // TODO: Null
        return null;
    }

    @Override
    public Void visit(Declaration d) {
        for (String s: d.getIdentifiers()) {
            if(symbolTable.containsKey(s)) {
                throw new IllegalStateException("Already declared " + s);
            }
            Relation newRelation = Relation.unary(s);
            symbolTable.put(s, newRelation);
            instance.boundRelation(newRelation);
        }
        return null; // TODO: null
    }

    @Override
    public kodkod.ast.Formula visit(MultiplicityFact f) {
        kodkod.ast.Expression r = (kodkod.ast.Expression) f.getRelationExpression().accept(this);
        switch (f.getMultiplicity()) {
            case SOME: return r.some();
            case NO:   return r.no();
        }
        throw new IllegalStateException();
    }

    @Override
    public kodkod.ast.Formula visit(BinaryFormula bf) {
        kodkod.ast.Formula left = (kodkod.ast.Formula) bf.getLeft().accept(this);
        kodkod.ast.Formula right = (kodkod.ast.Formula) bf.getRight().accept(this);
        switch (bf.getOperator()) {
            case AND: return left.and(right);
            case OR:  return left.or(right);
            case IMPLIES: return left.implies(right);
            case IFF:   return left.iff(right);
        }
        throw new IllegalStateException();
    }

    @Override
    public kodkod.ast.Expression visit(Rel rel) {
        if (rel == Rel.IDEN) {
            return instance.getIdentity();
        } else if (rel == Rel.UNIV) {
            return instance.getAtoms();
        }

        if (! symbolTable.containsKey(rel.getName())) {
            throw new IllegalStateException("Never declared: " + rel.getName());
        }

        return symbolTable.get(rel.getName());
    }

    @Override
    public kodkod.ast.Expression visit(BinaryRelationExpr binaryRelationExpr) {
        kodkod.ast.Expression left = (Expression) binaryRelationExpr.getLeft().accept(this);
        kodkod.ast.Expression right = (Expression) binaryRelationExpr.getRight().accept(this);

        switch (binaryRelationExpr.getOperator()) {
            case PLUS:  return left.union(right);
            case MINUS: return left.difference(right);
            case INTERSECTION: return left.intersection(right);
            case COMPOSE:   return compose(left, right);
        }

        throw new IllegalStateException();
    }

    private kodkod.ast.Expression compose(Expression left, Expression right) {
        return right.join(left.join(instance.getCycles()));
    }

    @Override
    public kodkod.ast.Expression visit(UnaryRelationExpr unaryRelationExpr) {
        kodkod.ast.Expression inner = (Expression) unaryRelationExpr.getOperand().accept(this);
        switch (unaryRelationExpr.getOperator()) {
            case CONVERSE: return inner.join(instance.getConverse());
        }

        throw new IllegalStateException();
    }

    @Override
    public kodkod.ast.Formula visit(NotFormula notFormula) {
        return ((kodkod.ast.Formula)notFormula.getOperand().accept(this)).not();
    }

    @Override
    public kodkod.ast.Formula visit(SetFormula setFormula) {
        kodkod.ast.Expression left = (Expression) setFormula.getLeft().accept(this);
        kodkod.ast.Expression right = (Expression) setFormula.getRight().accept(this);

        switch (setFormula.getOperator()) {
            case IN:    return left.in(right);
            case EQUALS: return left.eq(right);
        }

        throw new IllegalStateException();
    }
}

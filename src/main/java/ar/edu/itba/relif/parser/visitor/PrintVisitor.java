package ar.edu.itba.relif.parser.visitor;
import ar.edu.itba.relif.parser.ast.*;

public class PrintVisitor implements Visitor {

    @Override
    public void visit(Specification s) {
        for (Statement stmnt: s.getStatements()) {
            stmnt.accept(this);
            System.out.println();
        }
        s.getCommand().accept(this);
    }

    @Override
    public void visit(Command c) {
        System.out.println(c.toString());
    }

    @Override
    public void visit(Declaration d) {
        System.out.print("rel ");
        System.out.print(d.getIdentifiers().get(0));
        for (int i = 1; i < d.getIdentifiers().size(); i++) {
            String dec = d.getIdentifiers().get(i);
            System.out.print(", " + dec);
        }
    }

    @Override
    public void visit(MultiplicityFact f) {
        System.out.print(f.getMultiplicity() + " ");
        f.getRelationExpression().accept(this);
    }

    @Override
    public void visit(BinaryFormula bf) {
        System.out.print("(");
        bf.getLeft().accept(this);
        System.out.print(" " + bf.getOperator() + " ");
        bf.getRight().accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(Rel rel) {
        System.out.print(rel.getName());
    }

    @Override
    public void visit(BinaryRelationExpr be) {
        System.out.print("(");
        be.getLeft().accept(this);
        System.out.print(" " + be.getOperator() + " ");
        be.getRight().accept(this);
        System.out.print(")");
    }

    @Override
    public void visit(UnaryRelationExpr unaryRelationExpr) {
        System.out.print(unaryRelationExpr.getOperator());
        unaryRelationExpr.getOperand().accept(this);
    }

    @Override
    public void visit(NotFormula notFormula) {
        System.out.print("!");
        notFormula.getOperand().accept(this);
    }

    @Override
    public void visit(SetFormula sf) {
        System.out.print("(");
        sf.getLeft().accept(this);
        System.out.print(" " + sf.getOperator() + " ");
        sf.getRight().accept(this);
        System.out.print(")");
    }

}

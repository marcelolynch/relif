package ar.edu.itba.relif.parser;

import ar.edu.itba.relif.parser.ast.Specification;
import ar.edu.itba.relif.parser.visitor.PrintVisitor;
import ar.edu.itba.relif.parser.visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws Exception {
        Reader reader = new StringReader(
                "rel R\n" +
                "iden in R\n" +
                "R & R in iden\n" +
                "R.R in R\n" +
                "R + ~R = univ\n" +
                "some R - iden\n" +
                "run");
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        parser parser = new parser(new Scanner(reader, sf), sf);
        Symbol result = parser.parse();
        Specification spec = (Specification)result.value;
        Visitor printVisitor = new PrintVisitor();
        printVisitor.visit(spec);
    }
}

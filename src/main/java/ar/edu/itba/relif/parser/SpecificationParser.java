package ar.edu.itba.relif.parser;

import ar.edu.itba.relif.parser.ast.Specification;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java.io.Reader;
import java.util.Random;

public class SpecificationParser {
    public static Specification getSpecification(Reader reader) throws Exception {
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        parser parser = new parser(new Scanner(reader, sf), sf);
        Symbol result = parser.parse();
        Specification spec = (Specification)result.value;
        return spec;
    }

}

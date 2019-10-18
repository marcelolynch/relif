package ar.edu.itba.relif.parser;

import ar.edu.itba.relif.parser.ast.Specification;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java.io.Reader;
import java.util.Random;

/**
 * A utility class to parse relif specifications given text input
 */
public class SpecificationParser {

    /**
     * Given an input in a {@link Reader} it parses it with the Relif grammar
     * and returns the root of the AST (a {@link Specification}).
     *
     * @return a {@link Specification} resulting from a correct parse of the input
     *
      @see Specification
     */
    public static Specification getSpecification(Reader reader) throws Exception {
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        parser parser = new parser(new Scanner(reader, sf), sf);
        Symbol result = parser.parse();
        Specification spec = (Specification)result.value;
        return spec;
    }

}

package compiler.Exceptions;

import compiler.AST.Type;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String operator, Type type1, Type type2) {
        super("Invalid operation " + "operator " + "between types " + type1 + "and " + type2);
    }
}

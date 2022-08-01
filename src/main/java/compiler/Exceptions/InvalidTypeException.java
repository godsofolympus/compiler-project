package compiler.Exceptions;

import compiler.AST.Type;

public class InvalidTypeException extends RuntimeException{
    public InvalidTypeException(Type expected, Type provided) {
        super("Expected type of: \"" + expected.toString() + "\" but provided type was of: \"" + provided.toString()+ "\"");
    }
}

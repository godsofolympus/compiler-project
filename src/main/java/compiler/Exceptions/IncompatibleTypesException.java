package compiler.Exceptions;

import compiler.AST.Type;

import java.util.List;
import java.util.stream.Collectors;

public class IncompatibleTypesException extends RuntimeException{
    public IncompatibleTypesException(Type expected, Type provided) {
        super("Expected type of: \"" + expected.toString() + "\" but provided type was of: \"" + provided.toString()+ "\"");
    }
    public IncompatibleTypesException(List<Type> expectedTypes, Type provided) {
        super("Expected type of: \"" + expectedTypes.stream().map(Object::toString)
                .collect(Collectors.joining(", ")) + "\" but provided type was of: \"" + provided.toString()+ "\"");
    }
}

package compiler.Exceptions;

import compiler.AST.Type;

import java.util.List;
import java.util.stream.Collectors;

public class IncompatibleTypesException extends SemanticError{
    public IncompatibleTypesException(Type expected, Type provided) {
        super("Expected type of: \"" + expected.toString() + "\" but provided type was of: \"" + provided.toString()+ "\"");
    }
    public IncompatibleTypesException(List<Type> expectedTypes, Type provided) {
        super("Expected types of: \"" + expectedTypes.stream().map(Object::toString)
                .collect(Collectors.joining(" or ")) + "\" but provided type was of: \"" + provided.toString()+ "\"");
    }
}

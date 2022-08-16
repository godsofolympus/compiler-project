package compiler.Exceptions;

import compiler.AST.Type;

public class FieldNotFoundException extends SemanticError{
    public FieldNotFoundException(String name) {
        super("Can not resolve field " + name);
    }

    public FieldNotFoundException(String name, Type type) {
        super("Can not resolve field " + name + " in " + type);
    }

}

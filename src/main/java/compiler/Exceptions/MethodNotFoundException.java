package compiler.Exceptions;

import compiler.AST.Type;

public class MethodNotFoundException extends SemanticError{
    public MethodNotFoundException(String name) {
        super("Can not resolve method " + name);
    }

    public MethodNotFoundException(String name, Type type) {
        super("Can not resolve method " + name + "in " + type);
    }
}

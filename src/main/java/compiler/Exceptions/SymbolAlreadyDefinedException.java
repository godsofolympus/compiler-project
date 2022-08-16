package compiler.Exceptions;

public class SymbolAlreadyDefinedException extends SemanticError{
    public SymbolAlreadyDefinedException(String id) {
        super(id + " is already defined in scope");
    }
}

package compiler.Exceptions;

public class SymbolAlreadyDefinedException extends RuntimeException{
    public SymbolAlreadyDefinedException(String id) {
        super(id + " is already defined in scope");
    }
}

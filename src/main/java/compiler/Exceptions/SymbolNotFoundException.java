package compiler.Exceptions;

public class SymbolNotFoundException extends SemanticError{
    public SymbolNotFoundException(String symbol) {
        super("Symbol " + symbol + " is not found");
    }
}

package compiler.Exceptions;

public class SymbolNotFoundException extends RuntimeException{
    public SymbolNotFoundException(String symbol) {
        super("Symbol " + symbol + " is not found");
    }
}

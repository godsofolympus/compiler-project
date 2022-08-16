package compiler.Exceptions;

public class ArgumentNumberMismatchException extends SemanticError {
    public ArgumentNumberMismatchException(String id, int expectedN, int providedN) {
        super("method " + id + " expects " + expectedN + " arguments but " + providedN + " was provided.");
    }
}

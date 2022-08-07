package compiler.Exceptions;

public class ArgumentNumberMismatchException extends RuntimeException {
    public ArgumentNumberMismatchException(String id, int expectedN, int providedN) {
        super("method " + id + " expects " + expectedN + " arguments but " + providedN + " was provided.");
    }
}

package compiler.Exceptions;

public class NotCallableIdentifierException extends RuntimeException{
    public NotCallableIdentifierException(String id) {
        super("identifier " + id + " is not callable.");
    }
}

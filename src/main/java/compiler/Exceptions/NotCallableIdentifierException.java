package compiler.Exceptions;

public class NotCallableIdentifierException extends SemanticError{
    public NotCallableIdentifierException(String id) {
        super("identifier " + id + " is not callable.");
    }
}

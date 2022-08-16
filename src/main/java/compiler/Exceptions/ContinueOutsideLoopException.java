package compiler.Exceptions;

public class ContinueOutsideLoopException extends SemanticError{
    public ContinueOutsideLoopException() {
        super("continue outside loop");
    }
}

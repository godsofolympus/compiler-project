package compiler.Exceptions;

public class BreakOutsideLoopException extends SemanticError{
    public BreakOutsideLoopException() {
        super("break outside loop");
    }
}

package compiler.Exceptions;

public class ContinueOutsideLoopException extends RuntimeException{
    public ContinueOutsideLoopException() {
        super("continue outside loop");
    }
}

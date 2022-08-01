package compiler.Exceptions;

public class BreakOutsideLoopException extends RuntimeException{
    public BreakOutsideLoopException() {
        super("break outside loop");
    }
}

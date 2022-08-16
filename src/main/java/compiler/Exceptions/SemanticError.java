package compiler.Exceptions;

public abstract class SemanticError extends RuntimeException{
    public SemanticError(String s) {
        super(s);
    }
}

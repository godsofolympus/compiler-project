package compiler.Exceptions;

public class ThisOutsideClassException extends SemanticError{
    public ThisOutsideClassException() {
        super("this statement outside class");
    }
}

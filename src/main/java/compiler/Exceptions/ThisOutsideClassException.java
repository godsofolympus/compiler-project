package compiler.Exceptions;

public class ThisOutsideClassException extends RuntimeException{
    public ThisOutsideClassException() {
        super("this statement outside class");
    }
}

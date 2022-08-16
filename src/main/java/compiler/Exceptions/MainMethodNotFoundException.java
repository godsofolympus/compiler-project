package compiler.Exceptions;

public class MainMethodNotFoundException extends SemanticError{
    public MainMethodNotFoundException() {
        super("main method not found.");
    }
}

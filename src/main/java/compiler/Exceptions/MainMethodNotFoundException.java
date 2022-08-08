package compiler.Exceptions;

public class MainMethodNotFoundException extends RuntimeException{
    public MainMethodNotFoundException() {
        super("main method not found.");
    }
}

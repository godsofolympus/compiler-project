package compiler.Exceptions;

public class InterfaceNotFoundException extends RuntimeException{
    public InterfaceNotFoundException(String id) {
        super("inteface " + id + "is not found");
    }
}

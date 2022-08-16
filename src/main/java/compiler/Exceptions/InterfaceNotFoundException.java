package compiler.Exceptions;

public class InterfaceNotFoundException extends SemanticError{
    public InterfaceNotFoundException(String id) {
        super("inteface " + id + "is not found");
    }
}

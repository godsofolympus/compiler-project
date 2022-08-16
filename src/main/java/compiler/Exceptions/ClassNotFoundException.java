package compiler.Exceptions;

public class ClassNotFoundException extends SemanticError{
    public ClassNotFoundException(String classId) {
        super("class " + classId + " not found.");
    }
}

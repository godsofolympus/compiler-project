package compiler.Exceptions;

public class ClassNotFoundException extends RuntimeException{
    public ClassNotFoundException(String classId) {
        super("class " + classId + " not found.");
    }
}

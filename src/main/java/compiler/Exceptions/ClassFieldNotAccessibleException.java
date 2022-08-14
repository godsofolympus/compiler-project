package compiler.Exceptions;

import compiler.AST.ClassField;

public class ClassFieldNotAccessibleException extends RuntimeException{
    public ClassFieldNotAccessibleException(ClassField field, String className) {
        super(field.id + " has " + field.accessMode + " access in " + className);
    }
}

package compiler.Exceptions;

public class ReturnOutsideFunctionException extends SemanticError{
    public ReturnOutsideFunctionException() {
        super("return statement outside function");
    }
}

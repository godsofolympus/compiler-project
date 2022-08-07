package compiler.Exceptions;

public class ReturnOutsideFunctionException extends RuntimeException{
    public ReturnOutsideFunctionException() {
        super("return statement outside function");
    }
}

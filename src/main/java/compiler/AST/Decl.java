package compiler.AST;

public abstract class Decl {

    public static VariableDecl variableDecl() {return new VariableDecl();}
    public static class VariableDecl extends Decl {}

    public static FunctionDecl functionDecl() {return new FunctionDecl();}
    public static class FunctionDecl extends Decl {}

    public static ClassDecl classDecl() {return new ClassDecl();}
    public static class ClassDecl extends Decl {}

    public static InterfaceDecl interfaceDecl() {return new InterfaceDecl();}
    public static class InterfaceDecl extends Decl {}
}

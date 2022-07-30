package compiler.AST;

import java.util.List;

public abstract class Decl {

    public static VariableDecl variableDecl(Variable variable) {return new VariableDecl(variable);}
    public static class VariableDecl extends Decl {
        public Variable variable;

        public VariableDecl(Variable variable) {
            this.variable = variable;
        }
    }

    public static FunctionDecl functionDecl(String id, Type type, List<Variable> formals, StmtBlock stmtBlock) {
        return new FunctionDecl(id, type, formals, stmtBlock);
    }
    public static class FunctionDecl extends Decl {
        public String id;
        public Type type;
        public List<Variable> formals;
        public StmtBlock stmtBlock;

        public FunctionDecl(String id, Type type, List<Variable> formals, StmtBlock stmtBlock) {
            this.id = id;
            this.type = type;
            this.formals = formals;
            this.stmtBlock = stmtBlock;
        }
    }

    public static ClassDecl classDecl() {return new ClassDecl();}
    public static class ClassDecl extends Decl {}

    public static InterfaceDecl interfaceDecl() {return new InterfaceDecl();}
    public static class InterfaceDecl extends Decl {}
}

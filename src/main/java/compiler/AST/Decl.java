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

    public static ClassDecl classDecl(String id, String superClass, List<String> interfaces, List<ClassField> classFields)
    {return new ClassDecl(id, superClass, interfaces, classFields);}
    public static class ClassDecl extends Decl {
        public String id;
        public String superClass;
        public List<String> interfaces;
        public List<ClassField> classFields;

        public ClassDecl(String id, String superClass, List<String> interfaces, List<ClassField> classFields) {
            this.id = id;
            this.superClass = superClass;
            this.interfaces = interfaces;
            this.classFields = classFields;
        }
    }

    public static InterfaceDecl interfaceDecl(String id, List<Prototype> prototypes) {return new InterfaceDecl(id, prototypes);}
    public static class InterfaceDecl extends Decl {
        public String id;
        public List<Prototype> prototypes;

        public InterfaceDecl(String id, List<Prototype> prototypes) {
            this.id = id;
            this.prototypes = prototypes;
        }
    }
}

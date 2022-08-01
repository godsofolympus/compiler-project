package compiler.AST;

import compiler.models.Scope;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

import java.util.List;

public abstract class Decl implements Visitable {
    public String id;

    public Decl(String id) {
        this.id = id;
    }

    public static VariableDecl variableDecl(Variable variable) {return new VariableDecl(variable);}
    public static class VariableDecl extends Decl {
        public Variable variable;

        public VariableDecl(Variable variable) {
            super(variable.id);
            this.variable = variable;
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }

    public static FunctionDecl functionDecl(String id, Type type, List<Variable> formals, StmtBlock stmtBlock) {
        return new FunctionDecl(id, type, formals, stmtBlock);
    }
    public static class FunctionDecl extends Decl {
        public Type type;
        public List<Variable> formals;
        public StmtBlock stmtBlock;

        public FunctionDecl(String id, Type type, List<Variable> formals, StmtBlock stmtBlock) {
            super(id);
            this.type = type;
            this.formals = formals;
            this.stmtBlock = stmtBlock;
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }

    public static ClassDecl classDecl(String id, String superClass, List<String> interfaces, List<ClassField> classFields)
    {return new ClassDecl(id, superClass, interfaces, classFields);}
    public static class ClassDecl extends Decl {
        public String superClass;
        public List<String> interfaces;
        public List<ClassField> classFields;

        public ClassDecl(String id, String superClass, List<String> interfaces, List<ClassField> classFields) {
            super(id);
            this.superClass = superClass;
            this.interfaces = interfaces;
            this.classFields = classFields;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        public boolean isSubClassOf(String otherClassId) {
            if (this.superClass == null) return false;
            if (this.superClass.equals(otherClassId)) return true;
            ClassDecl parentClass = (ClassDecl) Scope.getInstance().getEntry(this.superClass);
            return parentClass.isSubClassOf(otherClassId);
        }
    }

    public static InterfaceDecl interfaceDecl(String id, List<Prototype> prototypes) {return new InterfaceDecl(id, prototypes);}
    public static class InterfaceDecl extends Decl {
        public List<Prototype> prototypes;

        public InterfaceDecl(String id, List<Prototype> prototypes) {
            super(id);
            this.prototypes = prototypes;
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }
}

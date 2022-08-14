package compiler.AST;

import compiler.models.Context;
import compiler.models.ContextualScoped;
import compiler.models.Scope;
import compiler.models.Typed;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Decl implements Visitable, Typed {
    public String id;

    public Decl(String id) {
        this.id = id;
    }

    public static VariableDecl variableDecl(Variable variable) {return new VariableDecl(variable);}
    public static VariableDecl variableDecl(Variable variable, int offset) {
        VariableDecl variableDecl = new VariableDecl(variable);
        variableDecl.offset = offset;
        return variableDecl;
    }
    public static class VariableDecl extends Decl {
        public Variable variable;
        public int offset;

        public boolean isGlobal;

        public VariableDecl(Variable variable) {
            super(variable.id);
            this.variable = variable;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public Type getType() {
            return variable.type;
        }
    }

    public static FunctionDecl functionDecl(String id, Type type, List<Variable> formals, StmtBlock stmtBlock) {
        return new FunctionDecl(id, type, formals, stmtBlock);
    }
    public static class FunctionDecl extends Decl implements ContextualScoped {
        public Type returnType;
        public List<Variable> formals;
        public StmtBlock stmtBlock;

        private int offsetCounter;

        public FunctionDecl(String id, Type returnType, List<Variable> formals, StmtBlock stmtBlock) {
            super(id);
            this.returnType = returnType;
            this.formals = formals;
            this.stmtBlock = stmtBlock;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public Type getType() {
            return returnType;
        }

        @Override
        public Context getContext() {
            return Context.FUNCTION;
        }

        public int getSizeofParameters() {
            int sum = 0;
            for (Variable formal : this.formals) {
                sum += formal.type.getSize();
            }
            return sum;
        }

        public int getOffsetCounter() {
            return offsetCounter;
        }

        public void setOffsetCounter(int offsetCounter) {
            this.offsetCounter = offsetCounter;
        }
    }

    public static ClassDecl classDecl(String id, String superClass, List<String> interfaces, List<ClassField> classFields)
    {return new ClassDecl(id, superClass, interfaces, classFields);}
    public static class ClassDecl extends Decl implements ContextualScoped {
        public String superClass;
        public List<String> interfaces;
        public List<ClassField> classFields;

        private final List<ClassField.VarField> varFields;
        private final List<ClassField.MethodField> methodFields;
        private Map<String, ClassField.VarField> varFieldMap;

        public ClassDecl(String id, String superClass, List<String> interfaces, List<ClassField> classFields) {
            super(id);
            this.superClass = superClass;
            this.interfaces = interfaces;
            this.classFields = classFields;
            this.varFields = new ArrayList<>();
            this.methodFields = new ArrayList<>();
            this.setVarFields();
            this.setMethodFields();
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        private void setVarFields() {
            for (ClassField classField : classFields) {
                if (classField instanceof ClassField.VarField)
                    this.varFields.add((ClassField.VarField) classField);
            }
        }

        public List<ClassField.VarField> getVarFields() {
            return varFields;
        }

        private void setMethodFields() {
            for (ClassField classField : classFields) {
                if (classField instanceof ClassField.MethodField)
                    this.methodFields.add((ClassField.MethodField) classField);
            }
        }

        public List<ClassField.MethodField> getMethodFields() {
            return methodFields;
        }

        public Map<String, ClassField.VarField> getVarFieldMap() {
            if (this.varFieldMap != null) return varFieldMap;
            int initOffset = 0;
            if (superClass == null) this.varFieldMap = new HashMap<>();
            else {
                this.varFieldMap = new HashMap<>(getSuperClass().getVarFieldMap());
                initOffset += getSuperClass().getRequiredSpace();
            }
            for (ClassField.VarField varField : varFields) {
                varField.setOffset(initOffset);
                varFieldMap.put(varField.id, varField);
                initOffset += varField.getSize();
            }
            return varFieldMap;
        }

        public ClassDecl getSuperClass() {
            if (this.superClass == null) return null;
            return (ClassDecl) Scope.getInstance().getEntry(this.superClass);
        }

        public boolean isSubClassOf(String otherClassId) {
            if (this.id.equals(otherClassId)) return true;
            if (this.superClass == null) return false;
            ClassDecl parentClass = this.getSuperClass();
            return parentClass.isSubClassOf(otherClassId);
        }

        public FunctionDecl getMethod(String id) {
            //TODO check access
            for (ClassField.MethodField methodField : this.methodFields) {
                if (methodField.id.equals(id))
                    return (FunctionDecl) methodField.decl;
            }
            if (superClass == null) return null;
            ClassDecl parentClass = (ClassDecl) Scope.getInstance().getEntry(this.superClass);
            return parentClass.getMethod(id);
        }

        public int getRequiredSpace() {
            int sum = 0;
            if (this.superClass != null)
                sum += this.getSuperClass().getRequiredSpace();
            for (ClassField.VarField varField : varFields) {
                sum += varField.decl.getType().getSize();
            }
            return sum;
        }

        @Override
        public Type getType() {
            return Type.nonPrimitiveType(id);
        }

        @Override
        public Context getContext() {
            return Context.CLASS;
        }
    }

    public static InterfaceDecl interfaceDecl(String id, List<Prototype> prototypes) {return new InterfaceDecl(id, prototypes);}
    public static class InterfaceDecl extends Decl implements ContextualScoped {
        public List<Prototype> prototypes;

        public InterfaceDecl(String id, List<Prototype> prototypes) {
            super(id);
            this.prototypes = prototypes;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return Type.nonPrimitiveType(id);
        }

        @Override
        public Context getContext() {
            return Context.INTERFACE;
        }
    }
}

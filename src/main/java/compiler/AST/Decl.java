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
    public static class VariableDecl extends Decl {
        public Variable variable;
        private int offset;

        public boolean isGlobal;
        public boolean isInstanceVar;

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

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

    public static FunctionDecl functionDecl(String id, Type type, List<Variable> formals, StmtBlock stmtBlock, int initOffset) {
        return new FunctionDecl(id, type, formals, stmtBlock, initOffset);
    }
    public static class FunctionDecl extends Decl implements ContextualScoped {
        public Type returnType;
        public List<Variable> formals;
        public StmtBlock stmtBlock;

        public boolean isMain;
        private int offsetCounter;

        private String label;


        public FunctionDecl(String id, Type returnType, List<Variable> formals, StmtBlock stmtBlock, int initOffset) {
            super(id);
            this.returnType = returnType;
            this.formals = formals;
            this.stmtBlock = stmtBlock;
            this.offsetCounter = initOffset;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
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

        public int getSizeofParameters(boolean isMethodField) {
            int sum = isMethodField ? 4 : 0;
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
        private Map<String, ClassField> fieldMap;

        private String vTablePtr;

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

        public String getvTablePtr() {
            return vTablePtr;
        }

        public void setvTablePtr(String vTablePtr) {
            this.vTablePtr = vTablePtr;
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

        public Map<String, ClassField> getFieldMap() {
            if (this.fieldMap != null) return fieldMap;
            int initFieldOffset = 0;
            int initMethodOffset = 0;
            if (superClass == null) this.fieldMap = new HashMap<>();
            else {
                this.fieldMap = new HashMap<>(getSuperClass().getFieldMap());
                initFieldOffset += getSuperClass().getRequiredSpaceForVars();
                initMethodOffset += getSuperClass().getRequiredSpaceForMethods();
            }
            for (ClassField.VarField varField : varFields) {
                varField.setOffset(initFieldOffset);
                fieldMap.put(varField.id, varField);
                initFieldOffset += varField.getSize();
            }
            for (ClassField.MethodField methodField : methodFields) {
                methodField.setOffset(initMethodOffset);
                fieldMap.put(methodField.id, methodField);
                initMethodOffset += methodField.getSize();
            }
            return fieldMap;
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

        public int getRequiredSpaceForVars() {
            int sum = 0;
            if (this.superClass != null)
                sum += this.getSuperClass().getRequiredSpaceForVars();
            for (ClassField.VarField varField : varFields) {
                sum += varField.getSize();
            }
            return sum;
        }

        public int getRequiredSpaceForMethods() {
            int sum = 0;
            if (this.superClass != null)
                sum += this.getSuperClass().getRequiredSpaceForMethods();
            for (ClassField.MethodField methodField : methodFields) {
                sum += methodField.getSize();
            }
            return sum;
        }

        public List<ClassField.MethodField> getInheritedMethods() {
            List<ClassField.MethodField> inheritedMethods = new ArrayList<>();
            for (ClassField field : getFieldMap().values()) {
                if (field instanceof ClassField.MethodField)
                    inheritedMethods.add((ClassField.MethodField) field);
            }
            return inheritedMethods;
        }

        public List<ClassField.VarField> getInheritedFields() {
            List<ClassField.VarField> inheritedFields = new ArrayList<>();
            for (ClassField field : getFieldMap().values()) {
                if (field instanceof ClassField.VarField)
                    inheritedFields.add((ClassField.VarField) field);
            }
            return inheritedFields;
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

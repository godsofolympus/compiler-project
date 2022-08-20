package compiler.AST;

import compiler.models.AccessMode;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

public abstract class ClassField implements Visitable {
    public AccessMode accessMode;
    public String id;
    public Decl decl;

    private int offset;

    public abstract int getSize();

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    public ClassField(AccessMode accessMode, String id) {
        this.accessMode = accessMode;
        this.id = id;
    }

    public static VarField varField(AccessMode accessMode, Decl.VariableDecl variableDecl) {return new VarField(accessMode, variableDecl); }

    public static class VarField extends ClassField {

        public VarField(AccessMode accessMode, Decl.VariableDecl varDecl) {
            super(accessMode, varDecl.id);
            varDecl.isInstanceVar = true;
            this.decl = varDecl;
        }


        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public int getSize() {
            return  decl.getType().getSize();
        }
    }

    public static MethodField methodField(AccessMode accessMode, Decl.FunctionDecl functionDecl) {return new MethodField(accessMode, functionDecl);}

    public static class MethodField extends ClassField {
        private String methodLabel;
        public MethodField(AccessMode accessMode, Decl.FunctionDecl functionDecl) {
            super(accessMode, functionDecl.id);
            Decl.FunctionDecl newFunctionDecl = Decl.functionDecl(functionDecl.id, functionDecl.returnType, functionDecl.formals, functionDecl.stmtBlock, -4);
            newFunctionDecl.isInstanceMethod = true;
            this.decl = newFunctionDecl;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public int getSize() {
            return 4;
        }

    }
}
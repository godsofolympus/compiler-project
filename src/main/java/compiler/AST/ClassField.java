package compiler.AST;

import compiler.models.AccessMode;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

public abstract class ClassField implements Visitable {
    public AccessMode accessMode;
    public String id;

    public ClassField(AccessMode accessMode, String id) {
        this.accessMode = accessMode;
        this.id = id;
    }

    public static VarField varField(AccessMode accessMode, Decl.VariableDecl variableDecl) {return new VarField(accessMode, variableDecl); }

    public static class VarField extends ClassField {
        public Decl.VariableDecl varDecl;
        public VarField(AccessMode accessMode, Decl.VariableDecl varDecl) {
            super(accessMode, varDecl.id);
            this.varDecl = varDecl;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static MethodField methodField(AccessMode accessMode, Decl.FunctionDecl functionDecl) {return new MethodField(accessMode, functionDecl);}

    public static class MethodField extends ClassField {
        public Decl.FunctionDecl functionDecl;
        public MethodField(AccessMode accessMode, Decl.FunctionDecl functionDecl) {
            super(accessMode, functionDecl.id);
            this.functionDecl = functionDecl;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }
}
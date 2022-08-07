package compiler.AST;

import compiler.models.AccessMode;

public abstract class ClassField {
    public AccessMode accessMode;
    public String id;

    public ClassField(AccessMode accessMode, String id) {
        this.accessMode = accessMode;
        this.id = id;
    }

    public static VarField varField(AccessMode accessMode, Variable variable) {return new VarField(accessMode, variable); }

    public static class VarField extends ClassField {
        public Variable varDecl;
        public VarField(AccessMode accessMode, Variable varDecl) {
            super(accessMode, varDecl.id);
            this.varDecl = varDecl;
        }
    }

    public static MethodField methodField(AccessMode accessMode, Decl.FunctionDecl functionDecl) {return new MethodField(accessMode, functionDecl);}

    public static class MethodField extends ClassField {
        public Decl.FunctionDecl functionDecl;
        public MethodField(AccessMode accessMode, Decl.FunctionDecl functionDecl) {
            super(accessMode, functionDecl.id);
            this.functionDecl = functionDecl;
        }
    }
}
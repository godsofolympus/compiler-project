package compiler.AST;

import compiler.models.Scope;
import compiler.models.Typed;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

public abstract class LValue implements Visitable, Typed {

    public abstract int getOffset();
    public static SimpleLVal simpleLVal(String id) {return new SimpleLVal(id);}
    public static class SimpleLVal extends LValue {
        public String id;

        public SimpleLVal(String id) {
            this.id = id;
        }

        @Override
        public Type getType() {
            return Scope.getInstance().getEntry(id).getType();
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public int getOffset() {
            Decl.VariableDecl variableDecl = (Decl.VariableDecl) Scope.getInstance().getEntry(id);
            return variableDecl.getOffset();
        }
    }

    public static DottedLVal dottedLVal(Expr expr, String id) {return new DottedLVal(expr, id);}
    public static class DottedLVal extends LValue {
        public Expr expr;
        public String id;

        public DottedLVal(Expr expr, String id) {
            this.expr = expr;
            this.id = id;
        }

        @Override
        public Type getType() {
            String classId = ((Type.NonPrimitiveType) this.expr.getType()).id;
            Decl.ClassDecl classDecl = (Decl.ClassDecl) Scope.getInstance().getEntry(classId);
            return classDecl.getFieldMap().get(this.id).decl.getType();
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public int getOffset() {
            String classId = ((Type.NonPrimitiveType) this.expr.getType()).id;
            Decl.ClassDecl classDecl = (Decl.ClassDecl) Scope.getInstance().getEntry(classId);
            return ((ClassField.VarField) classDecl.getFieldMap().get(this.id)).getOffset();
        }
    }

    public static IndexedLVal indexedLVal(Expr expr, Expr index) {return new IndexedLVal(expr, index);}
    public static class IndexedLVal extends LValue{
        public Expr expr;
        public Expr index;

        public IndexedLVal(Expr expr, Expr index) {
            this.expr = expr;
            this.index = index;
        }

        @Override
        public Type getType() {
            return ((Type.ArrayType) expr.getType()).baseType;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public int getOffset() {
            return 0;
        }
    }
}

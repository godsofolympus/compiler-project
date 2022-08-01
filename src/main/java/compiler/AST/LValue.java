package compiler.AST;

import compiler.models.Scope;
import compiler.models.Typed;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

public abstract class LValue implements Visitable, Typed {

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
            return null;
        }

        @Override
        public void accept(Visitor visitor) {

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
            return null;
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }
}

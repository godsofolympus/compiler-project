package compiler.AST;

import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

import java.util.List;

public abstract class Call implements Visitable {

    public String id;
    public List<Expr> actuals;

    public Call(String id, List<Expr> actuals) {
        this.id = id;
        this.actuals = actuals;
    }

    public static SimpleCall simpleCall(String id, List<Expr> actuals) {return new SimpleCall(id, actuals);}
    public static class SimpleCall extends Call {

        public SimpleCall(String id, List<Expr> actuals) {
            super(id, actuals);
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static DottedCall dottedCall(Expr expr, String id, List<Expr> actuals) {return new DottedCall(expr, id, actuals);}

    public static class DottedCall extends Call {
        public Expr expr;

        public DottedCall(Expr expr, String id, List<Expr> actuals) {
            super(id, actuals);
            this.expr = expr;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }
}

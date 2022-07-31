package compiler.AST;

import java.util.List;

public abstract class Call {

    public static SimpleCall simpleCall(String id, List<Expr> actuals) {return new SimpleCall(id, actuals);}
    public static class SimpleCall extends Call {
        public String id;
        public List<Expr> actuals;

        public SimpleCall(String id, List<Expr> actuals) {
            this.id = id;
            this.actuals = actuals;
        }
    }

    public static DottedCall dottedCall(Expr expr, String id, List<Expr> actuals) {return new DottedCall(expr, id, actuals);}

    public static class DottedCall extends Call {
        public Expr expr;
        public String id;
        public List<Expr> actuals;

        public DottedCall(Expr expr, String id, List<Expr> actuals) {
            this.expr = expr;
            this.id = id;
            this.actuals = actuals;
        }
    }
}

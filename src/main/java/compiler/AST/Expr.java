package compiler.AST;

import compiler.models.Typed;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

public abstract class Expr implements Visitable, Typed {
    public static AssignExpr assignExpr(LValue left, Expr right) {return new AssignExpr(left, right);}
    public static class AssignExpr extends Expr {
        public LValue leftHandSide;
        public Expr rightHandSide;

        public AssignExpr(LValue leftHandSide, Expr rightHandSide) {
            this.leftHandSide = leftHandSide;
            this.rightHandSide = rightHandSide;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return Type.nullType();
        }
    }

    public static ConstExpr constExpr(Constant constant) {return new ConstExpr(constant);}
    public static class ConstExpr extends Expr {
        public Constant constant;

        public ConstExpr(Constant constant) {
            this.constant = constant;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return constant.getType();
        }
    }

    public static LValExpr lValExpr(LValue lValue) {return new LValExpr(lValue);}
    public static class LValExpr extends Expr {
        public LValue lValue;

        public LValExpr(LValue lValue) {
            this.lValue = lValue;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return lValue.getType();
        }
    }

    public static ThisExpr thisExpr() {return new ThisExpr();}
    public static class ThisExpr extends Expr {
        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return null;
        }
    }

    public static CallExpr callExpr(Call call) {return new CallExpr(call);}
    public static class CallExpr extends Expr {
        public Call call;

        public CallExpr(Call call) {
            this.call = call;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return null;
        }
    }

    public static abstract class ArithExpr extends Expr {

        public Expr expr1;
        public Expr expr2;

        public ArithExpr(Expr expr1, Expr expr2) {
            this.expr1 = expr1;
            this.expr2 = expr2;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return null;
        }

        public static AddExpr addExpr(Expr expr1, Expr expr2) {return new AddExpr(expr1, expr2);}
        public static class AddExpr extends ArithExpr{
            public AddExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static SubExpr subExpr(Expr expr1, Expr expr2) {return new SubExpr(expr1, expr2);}
        public static class SubExpr extends ArithExpr{
            public SubExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static MultExpr multExpr(Expr expr1, Expr expr2) {return new MultExpr(expr1, expr2);}
        public static class MultExpr extends ArithExpr{
            public MultExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static DivExpr divExpr(Expr expr1, Expr expr2) {return new DivExpr(expr1, expr2);}
        public static class DivExpr extends ArithExpr{
            public DivExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static ModExpr modExpr(Expr expr1, Expr expr2) {return new ModExpr(expr1, expr2);}
        public static class ModExpr extends ArithExpr{
            public ModExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static MinusExpr minusExpr(Expr expr) {return new MinusExpr(expr);}
        public static class MinusExpr extends ArithExpr{
            public MinusExpr(Expr expr1) {
                super(expr1, null);
            }
        }
    }

    public static abstract class CompExpr extends Expr {

        public Expr expr1;
        public Expr expr2;

        public CompExpr(Expr expr1, Expr expr2) {
            this.expr1 = expr1;
            this.expr2 = expr2;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return null;
        }

        public static LessExpr lessExpr(Expr expr1, Expr expr2) {return new LessExpr(expr1, expr2);}
        public static class LessExpr extends CompExpr{
            public LessExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static LessEqExpr lessEqExpr(Expr expr1, Expr expr2) {return new LessEqExpr(expr1, expr2);}
        public static class LessEqExpr extends CompExpr{
            public LessEqExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static GreaterExpr greaterExpr(Expr expr1, Expr expr2) {return new GreaterExpr(expr1, expr2);}
        public static class GreaterExpr extends CompExpr{
            public GreaterExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static GreaterEqExpr greaterEqExpr(Expr expr1, Expr expr2) {return new GreaterEqExpr(expr1, expr2);}
        public static class GreaterEqExpr extends CompExpr{
            public GreaterEqExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static EqExpr eqExpr(Expr expr1, Expr expr2) {return new EqExpr(expr1, expr2);}
        public static class EqExpr extends CompExpr{
            public EqExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static NotEqExpr notEqExpr(Expr expr1, Expr expr2) {return new NotEqExpr(expr1, expr2);}
        public static class NotEqExpr extends CompExpr{
            public NotEqExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }
    }

    public static abstract class LogicalExpr extends Expr {

        public Expr expr1;
        public Expr expr2;

        public LogicalExpr(Expr expr1, Expr expr2) {
            this.expr1 = expr1;
            this.expr2 = expr2;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return null;
        }

        public static AndExpr andExpr(Expr expr1, Expr expr2) {return new AndExpr(expr1, expr2);}
        public static class AndExpr extends LogicalExpr {
            public AndExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static OrExpr orExpr(Expr expr1, Expr expr2) {return new OrExpr(expr1, expr2);}
        public static class OrExpr extends LogicalExpr {
            public OrExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
        }

        public static NotExpr notExpr(Expr expr) {return new NotExpr(expr);}
        public static class NotExpr extends LogicalExpr {
            public NotExpr(Expr expr1) {
                super(expr1, null);
            }
        }
    }

    public static abstract class FunctionExpr extends Expr {

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return null;
        }

        public static ReadIntExpr readIntExpr() {return new ReadIntExpr();}
        public static class ReadIntExpr extends FunctionExpr {}

        public static ReadLineExpr readLineExpr() {return new ReadLineExpr();}
        public static class ReadLineExpr extends FunctionExpr {}

        public static ItodExpr itodExpr(Expr expr) {return new ItodExpr(expr);}
        public static class ItodExpr extends FunctionExpr {
            public Expr expr;

            public ItodExpr(Expr expr) {
                this.expr = expr;
            }
        }

        public static DtoiExpr dtoiExpr(Expr expr) {return new DtoiExpr(expr);}
        public static class DtoiExpr extends FunctionExpr {
            public Expr expr;

            public DtoiExpr(Expr expr) {
                this.expr = expr;
            }
        }

        public static ItobExpr itobExpr(Expr expr) {return new ItobExpr(expr);}
        public static class ItobExpr extends FunctionExpr {
            public Expr expr;

            public ItobExpr(Expr expr) {
                this.expr = expr;
            }
        }

        public static BtoiExpr btoiExpr(Expr expr) {return new BtoiExpr(expr);}
        public static class BtoiExpr extends FunctionExpr {
            public Expr expr;

            public BtoiExpr(Expr expr) {
                this.expr = expr;
            }
        }
    }

    public static abstract class InitExpr extends Expr {

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return null;
        }

        public static ObjInit objInit(String id) {return new ObjInit(id);}
        public static class ObjInit extends InitExpr {
            public String id;

            public ObjInit(String id) {
                this.id = id;
            }
        }

        public static ArrInit arrInit(Expr expr, Type type) {return new ArrInit(expr, type);}
        public static class ArrInit extends InitExpr {
            public Expr expr;
            public Type type;

            public ArrInit(Expr expr, Type type) {
                this.expr = expr;
                this.type = type;
            }
        }
    }
}

package compiler.AST;

import compiler.models.Context;
import compiler.models.Scope;
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
            visitor.visit(this);
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
            visitor.visit(this);
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
            visitor.visit(this);
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
            visitor.visit(this);
        }

        @Override
        public Type getType() {
            Decl.ClassDecl classDecl = (Decl.ClassDecl)Scope.getInstance().getContext(Context.CLASS);
            return classDecl.getType();
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
            visitor.visit(this);
        }

        @Override
        public Type getType() {
            return this.call.getType();
        }
    }

    public static abstract class BinOpExpr extends Expr {

        public Expr expr1;
        public Expr expr2;

        public BinOpExpr(Expr expr1, Expr expr2) {
            this.expr1 = expr1;
            this.expr2 = expr2;
        }

        public static AddExpr addExpr(Expr expr1, Expr expr2) {
            return new AddExpr(expr1, expr2);}

        public static class AddExpr extends BinOpExpr {
            public AddExpr downcast() {
                if (this.expr1.getType().isLessThan(Type.PrimitiveType.integerType())) return intAddExpr(this.expr1, this.expr2);
                else if (this.expr1.getType().isLessThan(Type.PrimitiveType.doubleType())) return doubleAddExpr(this.expr1, this.expr2);
                else if (this.expr1.getType().isLessThan(Type.PrimitiveType.stringType())) return stringAddExpr(this.expr1, this.expr2);
                else return arrayAddExpr(this.expr1, this.expr2);
            };
            public AddExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return expr1.getType();
            }

            public static IntAddExpr intAddExpr(Expr expr1, Expr expr2) {return new IntAddExpr(expr1, expr2);}
            public static class IntAddExpr extends AddExpr {

                public IntAddExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }
            public static DoubleAddExpr doubleAddExpr(Expr expr1, Expr expr2) {return new DoubleAddExpr(expr1, expr2);}
            public static class DoubleAddExpr extends AddExpr {
                public DoubleAddExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static StringAddExpr stringAddExpr(Expr expr1, Expr expr2) {return new StringAddExpr(expr1, expr2);}
             public static class StringAddExpr extends AddExpr {
                 public StringAddExpr(Expr expr1, Expr expr2) {
                     super(expr1, expr2);
                 }

                 @Override
                 public void accept(Visitor visitor) {
                     visitor.visit(this);
                 }
             }

            public static ArrayAddExpr arrayAddExpr(Expr expr1, Expr expr2) {return new ArrayAddExpr(expr1, expr2);}
            public static class ArrayAddExpr extends AddExpr {
                public ArrayAddExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }
        }

        public static class ArithExpr extends BinOpExpr {

            public ArithExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return expr2.getType();
            }

            public static SubExpr subExpr(Expr expr1, Expr expr2) {return new SubExpr(expr1, expr2);}

            public static class SubExpr extends ArithExpr {
                public SubExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static MultExpr multExpr(Expr expr1, Expr expr2) {return new MultExpr(expr1, expr2);}
            public static class MultExpr extends ArithExpr {
                public MultExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static DivExpr divExpr(Expr expr1, Expr expr2) {return new DivExpr(expr1, expr2);}
            public static class DivExpr extends ArithExpr {
                public DivExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static ModExpr modExpr(Expr expr1, Expr expr2) {return new ModExpr(expr1, expr2);}
            public static class ModExpr extends ArithExpr {
                public ModExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }
        }

        public static class CompExpr extends BinOpExpr {
            public CompExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.booleanType();
            }

            public static LessExpr lessExpr(Expr expr1, Expr expr2) {return new LessExpr(expr1, expr2);}
            public static class LessExpr extends CompExpr {
                public LessExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }
                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static LessEqExpr lessEqExpr(Expr expr1, Expr expr2) {return new LessEqExpr(expr1, expr2);}
            public static class LessEqExpr extends CompExpr {
                public LessEqExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }
                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static GreaterExpr greaterExpr(Expr expr1, Expr expr2) {return new GreaterExpr(expr1, expr2);}
            public static class GreaterExpr extends CompExpr {
                public GreaterExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }
                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static GreaterEqExpr greaterEqExpr(Expr expr1, Expr expr2) {return new GreaterEqExpr(expr1, expr2);}
            public static class GreaterEqExpr extends CompExpr {
                public GreaterEqExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }
                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static EqExpr eqExpr(Expr expr1, Expr expr2) {return new EqExpr(expr1, expr2);}
            public static class EqExpr extends CompExpr {
                public EqExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static NotEqExpr notEqExpr(Expr expr1, Expr expr2) {return new NotEqExpr(expr1, expr2);}
            public static class NotEqExpr extends CompExpr {
                public NotEqExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }
                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }
        }

        public static class LogicalExpr extends BinOpExpr {

            public LogicalExpr(Expr expr1, Expr expr2) {
                super(expr1, expr2);
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.booleanType();
            }

            public static AndExpr andExpr(Expr expr1, Expr expr2) {return new AndExpr(expr1, expr2);}
            public static class AndExpr extends LogicalExpr {
                public AndExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }

            public static OrExpr orExpr(Expr expr1, Expr expr2) {return new OrExpr(expr1, expr2);}
            public static class OrExpr extends LogicalExpr {
                public OrExpr(Expr expr1, Expr expr2) {
                    super(expr1, expr2);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }
        }
    }

    public static abstract class UnOpExpr extends Expr {

        public Expr expr;

        public UnOpExpr(Expr expr) {
            this.expr = expr;
        }

        @Override
        public void accept(Visitor visitor) {

        }

        @Override
        public Type getType() {
            return expr.getType();
        }

        public static abstract class ArithExpr extends UnOpExpr {
            public ArithExpr(Expr expr) {
                super(expr);
            }
            public static MinusExpr minusExpr(Expr expr) {return new MinusExpr(expr);}
            public static class MinusExpr extends UnOpExpr {
                public MinusExpr(Expr expr) {
                    super(expr);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
            }
        }

        public static abstract class LogicalExpr extends UnOpExpr {
            public LogicalExpr(Expr expr) {
                super(expr);
            }

            public static NotExpr notExpr(Expr expr) {return new NotExpr(expr);}
            public static class NotExpr extends UnOpExpr {
                public NotExpr(Expr expr) {
                    super(expr);
                }

                @Override
                public void accept(Visitor visitor) {
                    visitor.visit(this);
                }
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
        public static class ReadIntExpr extends FunctionExpr {
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.integerType();
            }
        }

        public static ReadLineExpr readLineExpr() {return new ReadLineExpr();}
        public static class ReadLineExpr extends FunctionExpr {
            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.stringType();
            }
        }

        public static ItodExpr itodExpr(Expr expr) {return new ItodExpr(expr);}
        public static class ItodExpr extends FunctionExpr {
            public Expr expr;

            public ItodExpr(Expr expr) {
                this.expr = expr;
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.doubleType();
            }
        }

        public static DtoiExpr dtoiExpr(Expr expr) {return new DtoiExpr(expr);}
        public static class DtoiExpr extends FunctionExpr {
            public Expr expr;

            public DtoiExpr(Expr expr) {
                this.expr = expr;
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.integerType();
            }
        }

        public static ItobExpr itobExpr(Expr expr) {return new ItobExpr(expr);}
        public static class ItobExpr extends FunctionExpr {
            public Expr expr;

            public ItobExpr(Expr expr) {
                this.expr = expr;
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.booleanType();
            }
        }

        public static BtoiExpr btoiExpr(Expr expr) {return new BtoiExpr(expr);}
        public static class BtoiExpr extends FunctionExpr {
            public Expr expr;

            public BtoiExpr(Expr expr) {
                this.expr = expr;
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.PrimitiveType.integerType();
            }
        }
    }

    public static abstract class InitExpr extends Expr {

        @Override
        public void accept(Visitor visitor) {

        }

        public static ObjInit objInit(String id) {return new ObjInit(id);}
        public static class ObjInit extends InitExpr {
            public String id;

            public ObjInit(String id) {
                this.id = id;
            }

            @Override
            public Type getType() {
                return Type.nonPrimitiveType(id);
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
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

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Type getType() {
                return Type.arrayType(type);
            }
        }
    }
}

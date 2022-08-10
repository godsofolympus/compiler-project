package compiler.AST;

import compiler.models.Typed;
import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

public abstract class Constant implements Visitable, Typed {

    public static IntConst intConst(int value) {return new IntConst(value);}
    public static class IntConst extends Constant {
        public int value;
        public IntConst(int value) {
            this.value = value;
        }

        @Override
        public Type getType() {
            return Type.PrimitiveType.integerType();
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static BoolConst boolConst(boolean value) {return new BoolConst(value);}
    public static class BoolConst extends Constant {
        public boolean value;

        public BoolConst(boolean value) {
            this.value = value;
        }

        @Override
        public Type getType() {
            return Type.PrimitiveType.booleanType();
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static DoubleConst doubleConst(double value) {return new DoubleConst(value);}
    public static class DoubleConst extends Constant{
        public double value;

        public DoubleConst(double value) {
            this.value = value;
        }

        @Override
        public Type getType() {
            return Type.PrimitiveType.doubleType();
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }

    public static StringConst stringConst(String value) {return new StringConst(value);}
    public static class StringConst extends Constant {
        public String value;

        public StringConst(String value) {
            this.value = value;
        }

        @Override
        public Type getType() {
            return Type.PrimitiveType.stringType(value);
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }

    public static NullConst nullConst() {return new NullConst();}
    public static class NullConst extends Constant {
        @Override
        public Type getType() {
            return Type.PrimitiveType.nullType();
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }
}

package compiler.AST;

import compiler.models.Primitive;
import compiler.models.Typed;

public abstract class Constant implements Typed {

    public static IntConst intConst(Integer value) {return new IntConst(value);}
    public static class IntConst extends Constant {
        public Integer value;
        public IntConst(Integer value) {
            this.value = value;
        }

        @Override
        public Type getType() {
            return Type.primitiveType(Primitive.INT);
        }
    }

    public static BoolConst boolConst(Boolean value) {return new BoolConst(value);}
    public static class BoolConst extends Constant {
        public Boolean value;

        public BoolConst(Boolean value) {
            this.value = value;
        }

        @Override
        public Type getType() {
            return Type.primitiveType(Primitive.BOOL);
        }
    }

    public static DoubleConst doubleConst(Double value) {return new DoubleConst(value);}
    public static class DoubleConst extends Constant{
        public Double value;

        public DoubleConst(Double value) {
            this.value = value;
        }

        @Override
        public Type getType() {
            return Type.primitiveType(Primitive.DOUBLE);
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
            return Type.primitiveType(Primitive.STRING);
        }
    }

    public static NullConst nullConst() {return new NullConst();}
    public static class NullConst extends Constant {
        @Override
        public Type getType() {
            return Type.nullType();
        }
    }
}

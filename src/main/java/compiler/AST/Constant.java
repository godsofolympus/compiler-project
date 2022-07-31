package compiler.AST;

public abstract class Constant {

    public static IntConst intConst(Integer value) {return new IntConst(value);}
    public static class IntConst extends Constant {
        public Integer value;
        public IntConst(Integer value) {
            this.value = value;
        }
    }

    public static BoolConst boolConst(Boolean value) {return new BoolConst(value);}
    public static class BoolConst extends Constant {
        public Boolean value;

        public BoolConst(Boolean value) {
            this.value = value;
        }
    }

    public static DoubleConst doubleConst(Double value) {return new DoubleConst(value);}
    public static class DoubleConst extends Constant{
        public Double value;

        public DoubleConst(Double value) {
            this.value = value;
        }
    }

    public static StringConst stringConst(String value) {return new StringConst(value);}
    public static class StringConst extends Constant {
        public String value;

        public StringConst(String value) {
            this.value = value;
        }
    }

    public static NullConst nullConst() {return new NullConst();}
    public static class NullConst extends Constant {}
}

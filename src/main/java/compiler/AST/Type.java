package compiler.AST;

import compiler.models.Scope;

public abstract class Type {
    public abstract boolean isLessThan(Type other);

    public static NonPrimitiveType nonPrimitiveType(String name) {
        return new NonPrimitiveType(name);
    }

    public static ArrayType arrayType(Type baseType) {
        return new ArrayType(baseType);
    }

    public static class PrimitiveType extends Type {

        @Override
        public boolean isLessThan(Type other) {
            return this.getClass() == other.getClass();
        }

        public static NumberType numberType() {return new NumberType();}

        public static class NumberType extends PrimitiveType {
            @Override
            public boolean isLessThan(Type other) {
                if (other.getClass() == NumberType.class) return true;
                return super.isLessThan(other);
            }

            public static IntegerType integerType() {return new IntegerType();}
            public static class IntegerType extends NumberType{}

            public static DoubleType doubleType() {return new DoubleType();}
            public static class DoubleType extends NumberType{}
        }

        public static BooleanType booleanType() {return new BooleanType();}
        public static class BooleanType extends PrimitiveType {}

        public static StringType stringType() {return new StringType();}
        public static class StringType extends PrimitiveType{}

    }

    public static class NonPrimitiveType extends Type {
        public String id;

        public NonPrimitiveType(String id) {
            this.id = id;
        }

        @Override
        public boolean isLessThan(Type other) {
            if (getClass() != other.getClass()) return false;
            String otherId = ((NonPrimitiveType) other).id;
            Decl.ClassDecl currentClass = (Decl.ClassDecl) Scope.getInstance().getEntry(this.id);
            return currentClass.isSubClassOf(otherId);
        }

        @Override
        public String toString() {
            return "NonPrimitiveType{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    public static class ArrayType extends Type {
        public Type baseType;

        public ArrayType(Type baseType) {
            this.baseType = baseType;
        }

        @Override
        public boolean isLessThan(Type other) {
            if (getClass() != other.getClass()) return false;
            return baseType.isLessThan(((ArrayType) other).baseType);
        }

        @Override
        public String toString() {
            return "ArrayType{" +
                    "baseType=" + baseType +
                    '}';
        }
    }

    public static VoidType voidType() {
        return new VoidType();
    }

    public static class VoidType extends Type {
        @Override
        public boolean isLessThan(Type other) {
            return false;
        }
    }

    public static NullType nullType() {
        return new NullType();
    }

    public static class NullType extends Type {

        @Override
        public boolean isLessThan(Type other) {
            return true;
        }

        @Override
        public String toString() {
            return "NullType{}";
        }
    }
}

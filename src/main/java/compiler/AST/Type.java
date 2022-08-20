package compiler.AST;

import compiler.models.Scope;

public abstract class Type {
    public abstract boolean isLessThan(Type other);
    public abstract int getSize();

    public static NonPrimitiveType nonPrimitiveType(String name) {
        return new NonPrimitiveType(name);
    }

    public static ArrayType arrayType(Type baseType) {
        return new ArrayType(baseType);
    }

    public static abstract class PrimitiveType extends Type {

        @Override
        public boolean isLessThan(Type other) {
            return this.getClass() == other.getClass();
        }

        public static IntegerType integerType() {return new IntegerType();}
        public static class IntegerType extends PrimitiveType{
            @Override
            public int getSize() {
                return 4;
            }
        }

        public static DoubleType doubleType() {return new DoubleType();}
        public static class DoubleType extends PrimitiveType{
            @Override
            public int getSize() {
                return 4;
            }
        }

        public static BooleanType booleanType() {return new BooleanType();}
        public static class BooleanType extends PrimitiveType {
            @Override
            public int getSize() {
                return 4;
            }
        }

        public static StringType stringType() {return new StringType();}
        public static class StringType extends PrimitiveType{

            @Override
            public int getSize() {
                return 4;
            }
        }

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
            Decl.ClassDecl currentClass = (Decl.ClassDecl) Scope.getInstance().getClass(this.id);
            return currentClass.isSubClassOf(otherId);
        }

        @Override
        public int getSize() {
            return 4;
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
            return other instanceof ArrayType && baseType.isLessThan(((ArrayType) other).baseType);
        }

        @Override
        public int getSize() {
            return 4;
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
            return getClass() == other.getClass();
        }

        @Override
        public int getSize() {
            return 0;
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
        public int getSize() {
            return 0;
        }

        @Override
        public String toString() {
            return "NullType{}";
        }
    }
}

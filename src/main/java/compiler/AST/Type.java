package compiler.AST;

import compiler.models.Primitive;
import compiler.models.Scope;

public abstract class Type {
    public abstract boolean isLessThan(Type other);

    public static PrimitiveType primitiveType(Primitive primitive) {return new PrimitiveType(primitive);}
    public static NonPrimitiveType nonPrimitiveType(String name) {return new NonPrimitiveType(name);}
    public static ArrayType arrayType(Type baseType) {return new ArrayType(baseType);}
    public static class PrimitiveType extends Type {
        public Primitive primitive;
        public PrimitiveType(Primitive primitive) {
            this.primitive = primitive;
        }

        @Override
        public boolean isLessThan(Type other) {
            if (getClass() != other.getClass()) return false;
            return this.primitive == ((PrimitiveType) other).primitive;
        }

        @Override
        public String toString() {
            return "PrimitiveType{" +
                    "primitive=" + primitive +
                    '}';
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

    public static NullType nullType() {return new NullType();}

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

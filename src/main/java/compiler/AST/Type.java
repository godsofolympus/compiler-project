package compiler.AST;

import compiler.models.Primitive;

public abstract class Type {

    public static PrimitiveType primitiveType(Primitive primitive) {return new PrimitiveType(primitive);}
    public static NonPrimitiveType nonPrimitiveType(String name) {return new NonPrimitiveType(name);}
    public static ArrayType arrayType(Type baseType) {return new ArrayType(baseType);}
    public static class PrimitiveType extends Type {
        public Primitive primitive;
        public PrimitiveType(Primitive primitive) {
            this.primitive = primitive;
        }
    }

    public static class NonPrimitiveType extends Type {
        public String name;
        public NonPrimitiveType(String name) {
            this.name = name;
        }
    }

    public static class ArrayType extends Type {
        public Type baseType;
        public ArrayType(Type baseType) {
            this.baseType = baseType;
        }
    }
}

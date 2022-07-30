package compiler.AST;

public class Variable {
    public Type type;
    public String id;
    public Variable(Type type, String id) {
        this.type = type;
        this.id = id;
    }
}

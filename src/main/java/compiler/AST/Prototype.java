package compiler.AST;

import java.util.List;

public class Prototype {
    public Type type;
    public String id;
    public List<Variable> formals;

    public Prototype(Type type, String id, List<Variable> formals) {
        this.type = type;
        this.id = id;
        this.formals = formals;
    }
}

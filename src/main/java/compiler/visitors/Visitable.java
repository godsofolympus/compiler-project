package compiler.visitors;

public interface Visitable {
    void accept(Visitor visitor);
}

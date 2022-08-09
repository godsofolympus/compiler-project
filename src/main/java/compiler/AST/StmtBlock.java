package compiler.AST;

import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

import java.util.List;

public class StmtBlock implements Visitable {

    public List<Decl.VariableDecl> variableDecls;
    public List<Stmt> stmts;

    public StmtBlock(List<Decl.VariableDecl> variableDecls, List<Stmt> stmts) {
        this.variableDecls = variableDecls;
        this.stmts = stmts;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    public int getRequiredSpace() {
        int sum = 0;
        for (Decl.VariableDecl variableDecl : this.variableDecls) {
            sum += variableDecl.variable.type.getSize();
        }
        for (Stmt stmt : this.stmts) {
            sum += stmt.getRequiredSpace();
        }
        return sum;
    }
}

package compiler.AST;

import compiler.visitors.Visitable;
import compiler.visitors.Visitor;

import java.util.List;

public class StmtBlock implements Visitable {

    public List<Variable> variableDecl;
    public List<Stmt> stmts;

    public StmtBlock(List<Variable> variableDecl, List<Stmt> stmts) {
        this.variableDecl = variableDecl;
        this.stmts = stmts;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

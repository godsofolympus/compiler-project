package compiler.AST;

import java.util.List;

public class StmtBlock {

    public List<Variable> variableDecl;
    public List<Stmt> stmts;

    public StmtBlock(List<Variable> variableDecl, List<Stmt> stmts) {
        this.variableDecl = variableDecl;
        this.stmts = stmts;
    }
}

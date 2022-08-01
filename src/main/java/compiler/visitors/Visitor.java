package compiler.visitors;

import compiler.AST.*;

public interface Visitor {
    void visit(Program program);
    void visit(Decl.VariableDecl variableDecl);
    void visit(Decl.FunctionDecl functionDecl);
    void visit(Stmt.IfStmt ifStmt);
    void visit(LValue lValue);
    void visit(LValue.SimpleLVal lValue);
    void visit(Expr expr);
    void visit(Expr.AssignExpr assignExpr);
}

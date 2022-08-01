package compiler.visitors;

import compiler.AST.Decl;
import compiler.AST.Program;
import compiler.AST.Stmt;

public interface Visitor {
    void visit(Program program);
    void visit(Decl.VariableDecl variableDecl);
    void visit(Decl.FunctionDecl functionDecl);

    void visit(Stmt.IfStmt ifStmt);
}

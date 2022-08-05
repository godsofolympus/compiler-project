package compiler.visitors;

import compiler.AST.Decl;
import compiler.AST.Decl.FunctionDecl;
import compiler.AST.Decl.VariableDecl;
import compiler.AST.Expr;
import compiler.AST.Expr.AssignExpr;
import compiler.AST.LValue;
import compiler.AST.LValue.SimpleLVal;
import compiler.AST.Program;
import compiler.AST.Stmt.BlockStmt;
import compiler.AST.Stmt.BreakStmt;
import compiler.AST.Stmt.ForStmt;
import compiler.AST.Stmt.IfStmt;
import compiler.AST.Stmt.WhileStmt;
import compiler.AST.StmtBlock;

public class CodeGeneratorVisitor implements Visitor{

    @Override
    public void visit(Program program) {
        for (Decl decl : program.decls) {
            decl.accept(this);
        }
    }

    @Override
    public void visit(VariableDecl variableDecl) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(FunctionDecl functionDecl) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(IfStmt ifStmt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(WhileStmt whileStmt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ForStmt forStmt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(BreakStmt breakStmt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(BlockStmt blockStmt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(StmtBlock stmtBlock) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(LValue lValue) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(SimpleLVal lValue) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(Expr expr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(AssignExpr assignExpr) {
        // TODO Auto-generated method stub
        
    }
    
}

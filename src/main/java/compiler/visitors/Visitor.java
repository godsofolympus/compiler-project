package compiler.visitors;

import compiler.AST.*;

public interface Visitor {
    void visit(Program program);
    void visit(Decl.VariableDecl variableDecl);
    void visit(Decl.FunctionDecl functionDecl);
    void visit(Stmt.ExprStmt exprStmt);
    void visit(Stmt.IfStmt ifStmt);
    void visit(Stmt.WhileStmt whileStmt);
    void visit(Stmt.ForStmt forStmt);
    void visit(Stmt.BreakStmt breakStmt);
    void visit(Stmt.ContinueStmt continueStmt);
    void visit(Stmt.ReturnStmt returnStmt);
    void visit(Stmt.PrintStmt printStmt);
    void visit(Stmt.BlockStmt blockStmt);
    void visit(StmtBlock stmtBlock);
    void visit(LValue lValue);
    void visit(LValue.SimpleLVal lValue);
    void visit(Expr.ConstExpr constExpr);
    void visit(Expr.ThisExpr thisExpr);
    void visit(Expr.CallExpr callExpr);
    void visit(Expr.AssignExpr assignExpr);
    void visit(Expr.LValExpr lValExpr);
    void visit(Expr.BinOpExpr binOpExpr);
    void visit(Expr.BinOpExpr.AddExpr addExpr);
    void visit(Expr.BinOpExpr.AddExpr.IntAddExpr intAddExpr);
    void visit(Expr.BinOpExpr.AddExpr.DoubleAddExpr doubleAddExpr);
    void visit(Expr.BinOpExpr.AddExpr.StringAddExpr stringAddExpr);
    void visit(Expr.BinOpExpr.AddExpr.ArrayAddExpr arrayAddExpr);
    void visit(Expr.BinOpExpr.ArithExpr arithExpr);
    void visit(Expr.BinOpExpr.CompExpr compExpr);
    void visit(Expr.BinOpExpr.CompExpr.LessExpr lessExpr);
    void visit(Expr.BinOpExpr.CompExpr.LessEqExpr lessEqExpr);
    void visit(Expr.BinOpExpr.CompExpr.GreaterExpr greaterExpr);
    void visit(Expr.BinOpExpr.CompExpr.GreaterEqExpr greaterEqExpr);
    void visit(Expr.BinOpExpr.CompExpr.EqExpr eqExpr);
    void visit(Expr.BinOpExpr.CompExpr.NotEqExpr notEqExpr);
    void visit(Expr.BinOpExpr.LogicalExpr logicalExpr);
    void visit(Call.SimpleCall simpleCall);
    void visit(Call.DottedCall dottedCall);
    void visit(Constant.IntConst intConst);
    void visit(Constant.BoolConst boolConst);
}

package compiler.visitors;

import compiler.AST.*;
import compiler.AST.Expr.FunctionExpr.BtoiExpr;
import compiler.AST.Expr.FunctionExpr.DtoiExpr;
import compiler.AST.Expr.FunctionExpr.ItobExpr;
import compiler.AST.Expr.FunctionExpr.ItodExpr;
import compiler.AST.Expr.FunctionExpr.ReadLineExpr;
import compiler.AST.Expr.InitExpr.ArrInit;

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
    void visit(LValue.SimpleLVal lValue);
    void visit(LValue.IndexedLVal lValue);
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
    void visit(Expr.BinOpExpr.ArithExpr.SubExpr subExpr);
    void visit(Expr.BinOpExpr.ArithExpr.MultExpr multExpr);
    void visit(Expr.BinOpExpr.ArithExpr.DivExpr divExpr);
    void visit(Expr.BinOpExpr.ArithExpr.ModExpr modExpr);
    void visit(Expr.BinOpExpr.CompExpr compExpr);
    void visit(Expr.BinOpExpr.CompExpr.LessExpr lessExpr);
    void visit(Expr.BinOpExpr.CompExpr.LessEqExpr lessEqExpr);
    void visit(Expr.BinOpExpr.CompExpr.GreaterExpr greaterExpr);
    void visit(Expr.BinOpExpr.CompExpr.GreaterEqExpr greaterEqExpr);
    void visit(Expr.BinOpExpr.CompExpr.EqExpr eqExpr);
    void visit(Expr.BinOpExpr.CompExpr.NotEqExpr notEqExpr);
    void visit(Expr.BinOpExpr.LogicalExpr logicalExpr);
    void visit(Expr.BinOpExpr.LogicalExpr.AndExpr andExpr);
    void visit(Expr.BinOpExpr.LogicalExpr.OrExpr orExpr);
    void visit(Expr.UnOpExpr.ArithExpr.MinusExpr minusExpr);
    void visit(Expr.UnOpExpr.LogicalExpr.NotExpr notExpr);
    void visit(Call.SimpleCall simpleCall);
    void visit(Call.DottedCall dottedCall);
    void visit(Constant.IntConst intConst);
    void visit(Constant.BoolConst boolConst);
    void visit(Constant.DoubleConst doubleConst);
    void visit(Constant.StringConst stringConst);
    void visit(Expr.FunctionExpr.ReadIntExpr readIntExpr);
    void visit(ReadLineExpr readLineExpr);
    void visit(ItodExpr itodExpr);
    void visit(DtoiExpr dtoiExpr);
    void visit(ItobExpr itobExpr);
    void visit(BtoiExpr btoiExpr);
    void visit(ArrInit arrInit);
}

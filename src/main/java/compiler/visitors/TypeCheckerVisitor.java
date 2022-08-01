package compiler.visitors;

import compiler.AST.*;
import compiler.Exceptions.BreakOutsideLoopException;
import compiler.Exceptions.InvalidTypeException;
import compiler.models.Context;
import compiler.models.Primitive;
import compiler.models.Scope;

public class TypeCheckerVisitor implements Visitor{

    @Override
    public void visit(Program program) {
        for (Decl decl : program.decls) {
            Scope.getInstance().setEntry(decl.id, decl);
        }
        for (Decl decl : program.decls) {
            decl.accept(this);
        }
    }

    @Override
    public void visit(Decl.VariableDecl variableDecl) {
        Scope.getInstance().setEntry(variableDecl.id, variableDecl);
    }

    @Override
    public void visit(Decl.FunctionDecl functionDecl) {
        Scope scope = Scope.pushScope(functionDecl);
        for (Variable formal : functionDecl.formals) {
            scope.setEntry(formal.id, Decl.variableDecl(formal));
        }
        functionDecl.stmtBlock.accept(this);
        Scope.popScope();
    }

    @Override
    public void visit(Stmt.IfStmt ifStmt) {
        ifStmt.cond.accept(this);
        if (!ifStmt.cond.getType().isLessThan(Type.primitiveType(Primitive.BOOL)))
            throw new InvalidTypeException(Type.primitiveType(Primitive.BOOL), ifStmt.cond.getType());
        ifStmt.stmt.accept(this);
        if (ifStmt.elseStmt != null) ifStmt.elseStmt.accept(this);
    }

    @Override
    public void visit(Stmt.WhileStmt whileStmt) {
        whileStmt.cond.accept(this);
        if (!whileStmt.cond.getType().isLessThan(Type.primitiveType(Primitive.BOOL)))
            throw new InvalidTypeException(Type.primitiveType(Primitive.BOOL), whileStmt.cond.getType());
        Scope.pushScope(whileStmt);
        whileStmt.stmt.accept(this);
        Scope.popScope();
    }

    @Override
    public void visit(Stmt.ForStmt forStmt) {
        if (forStmt.init != null) forStmt.init.accept(this);
        forStmt.cond.accept(this);
        if (!forStmt.cond.getType().isLessThan(Type.primitiveType(Primitive.BOOL)))
            throw new InvalidTypeException(Type.primitiveType(Primitive.BOOL), forStmt.cond.getType());
        Scope.pushScope(forStmt);
        if (forStmt.update != null) forStmt.update.accept(this);
        forStmt.stmt.accept(this);
        Scope.popScope();
    }

    @Override
    public void visit(Stmt.BreakStmt breakStmt) {
        if (Scope.getInstance().getContext(Context.LOOP) == null)
            throw new BreakOutsideLoopException();
    }

    @Override
    public void visit(Stmt.BlockStmt blockStmt) {
        blockStmt.stmtBlock.accept(this);
    }

    @Override
    public void visit(StmtBlock stmtBlock) {
        Scope scope = Scope.pushScope(null);
        for (Variable variable : stmtBlock.variableDecl) {
            scope.setEntry(variable.id, Decl.variableDecl(variable));
        }
        for (Stmt stmt : stmtBlock.stmts) {
            stmt.accept(this);
        }
        Scope.popScope();
    }

    @Override
    public void visit(LValue lValue) {
        lValue.accept(this);
    }

    @Override
    public void visit(LValue.SimpleLVal lValue) {}

    @Override
    public void visit(Expr expr) {
        expr.accept(this);
    }

    @Override
    public void visit(Expr.AssignExpr assignExpr) {
        assignExpr.leftHandSide.accept(this);
        assignExpr.rightHandSide.accept(this);
        Type leftType = assignExpr.leftHandSide.getType();
        Type rightType = assignExpr.rightHandSide.getType();
        if (!rightType.isLessThan(leftType))
            throw new InvalidTypeException(leftType, rightType);
    }
}

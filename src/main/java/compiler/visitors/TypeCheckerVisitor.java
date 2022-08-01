package compiler.visitors;

import compiler.AST.*;
import compiler.Exceptions.InvalidTypeException;
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
        Scope scope = Scope.getInstance().pushScope();
        for (Variable formal : functionDecl.formals) {
            scope.setEntry(formal.id, Decl.variableDecl(formal));
        }
        for (Variable variable : functionDecl.stmtBlock.variableDecl) {
            scope.setEntry(variable.id, Decl.variableDecl(variable));
        }
        for (Stmt stmt : functionDecl.stmtBlock.stmts) {
            stmt.accept(this);
        }
        scope.popScope();
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

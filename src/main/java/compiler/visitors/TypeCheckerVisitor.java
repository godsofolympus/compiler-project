package compiler.visitors;

import compiler.AST.Decl;
import compiler.AST.Program;
import compiler.AST.Stmt;
import compiler.AST.Variable;
import compiler.models.Scope;

public class TypeCheckerVisitor implements Visitor{
    private Scope currentScope = new Scope(null);

    @Override
    public void visit(Program program) {
        for (Decl decl : program.decls) {
            this.currentScope.symbolTable.put(decl.id, decl);
        }
        for (Decl decl : program.decls) {
            decl.accept(this);
        }
    }

    @Override
    public void visit(Decl.VariableDecl variableDecl) {
        this.currentScope.symbolTable.put(variableDecl.id, variableDecl);
    }

    @Override
    public void visit(Decl.FunctionDecl functionDecl) {
        this.currentScope = new Scope(this.currentScope);
        for (Variable formal : functionDecl.formals) {
            this.currentScope.symbolTable.put(formal.id, Decl.variableDecl(formal));
        }
        for (Variable variable : functionDecl.stmtBlock.variableDecl) {
            this.currentScope.symbolTable.put(variable.id, Decl.variableDecl(variable));
        }
        for (Stmt stmt : functionDecl.stmtBlock.stmts) {
            stmt.accept(this);
        }
        this.currentScope = this.currentScope.parent;
    }
}

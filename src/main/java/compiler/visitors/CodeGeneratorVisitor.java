package compiler.visitors;

import compiler.AST.*;
import compiler.AST.Decl.FunctionDecl;
import compiler.AST.Decl.VariableDecl;
import compiler.AST.Expr.AssignExpr;
import compiler.AST.LValue.SimpleLVal;
import compiler.AST.Stmt.BlockStmt;
import compiler.AST.Stmt.BreakStmt;
import compiler.AST.Stmt.ForStmt;
import compiler.AST.Stmt.IfStmt;
import compiler.AST.Stmt.WhileStmt;
import compiler.codegenerator.CodeGenerator;
import compiler.models.Context;
import compiler.models.Scope;

import java.util.List;

import static compiler.models.Register.*;


public class CodeGeneratorVisitor implements Visitor{

    private final CodeGenerator cgen = CodeGenerator.getInstance();

    @Override
    public void visit(Program program) {
        generateGlobalVars(program.getGlobalVars());
        for (FunctionDecl functionDecl : program.getFunctionDecls()) {
            if (functionDecl.id.equals("main")) {
                cgen.generated.append("\t.text\n")
                        .append("\t.globl main\n");
                cgen.genLabel("main");
                generateFunction(functionDecl);
            }
            else functionDecl.accept(this);
        }
    }

    public void generateGlobalVars(List<Variable> globalVars) {
        for (Variable globalVar : globalVars) {
            cgen.generated.append("\t.data\n")
                    .append("\t.align 2\n")
                    .append("_").append(globalVar.id).append(": ").append(".space ").append(globalVar.type.getSize()).append("\n");
        }
    }

    public void generateFunction(FunctionDecl functionDecl) {
        Scope.pushScope(functionDecl);
        int offsetCounter = 0;
        for (Variable formal : functionDecl.formals) {
            Scope.getInstance().setEntry(formal.id, Decl.variableDecl(formal, offsetCounter));
            offsetCounter -= formal.type.getSize();
        }
        functionDecl.setOffsetCounter(offsetCounter - 8);
        int paramSize = functionDecl.getSizeofParameters();
        int localsSize = functionDecl.stmtBlock.getRequiredSpace();
        this.generateFunctionEntry(paramSize, localsSize);
        functionDecl.stmtBlock.accept(this);
        this.generateFunctionExit(functionDecl.id, paramSize);
        Scope.popScope();
    }

    public void generateFunctionEntry(int paramSize, int localsSize) {
        cgen.genPush(RA);
        cgen.genPush(FP);
        cgen.generate("addu", FP, SP, String.valueOf(paramSize + 8));
        cgen.generate("subu", SP, SP, String.valueOf(localsSize));
    }

    public void generateFunctionExit(String functionName, int paramSize) {
        cgen.genLabel("_" + functionName + "_Exit");
        cgen.generateIndexed("lw", RA, FP, -paramSize);
        cgen.generate("move", T0, FP);
        cgen.generateIndexed("lw", FP, FP, -(paramSize + 4));
        cgen.generate("move", SP, T0);
        cgen.generate("jr", RA);
    }

    @Override
    public void visit(VariableDecl variableDecl) {
        FunctionDecl functionDecl = (FunctionDecl) Scope.getInstance().getContext(Context.FUNCTION);
        int offsetCounter = functionDecl.getOffsetCounter();
        Scope.getInstance().setEntry(variableDecl.id, variableDecl);
        offsetCounter -= variableDecl.variable.type.getSize();
        functionDecl.setOffsetCounter(offsetCounter);
    }

    @Override
    public void visit(FunctionDecl functionDecl) {
        cgen.genLabel("_" + functionDecl.id);
        generateFunction(functionDecl);
    }

    @Override
    public void visit(Stmt.ExprStmt exprStmt) {

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
    public void visit(Stmt.ContinueStmt continueStmt) {

    }

    @Override
    public void visit(Stmt.ReturnStmt returnStmt) {

    }

    @Override
    public void visit(Stmt.PrintStmt printStmt) {

    }

    @Override
    public void visit(BlockStmt blockStmt) {
        Scope.pushScope(null);
        blockStmt.stmtBlock.accept(this);
        Scope.popScope();
    }

    @Override
    public void visit(StmtBlock stmtBlock) {
        for (VariableDecl variableDecl : stmtBlock.variableDecls) {
            variableDecl.accept(this);
        }
        for (Stmt stmt : stmtBlock.stmts) {
            stmt.accept(this);
        }
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
    public void visit(Expr.ThisExpr thisExpr) {

    }

    @Override
    public void visit(Expr.CallExpr callExpr) {

    }


    @Override
    public void visit(AssignExpr assignExpr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr addExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr arithExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr compExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr logicalExpr) {

    }

    @Override
    public void visit(Call.SimpleCall simpleCall) {

    }

    @Override
    public void visit(Call.DottedCall dottedCall) {

    }

}

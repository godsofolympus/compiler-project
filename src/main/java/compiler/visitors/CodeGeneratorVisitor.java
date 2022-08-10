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
        variableDecl.offset = offsetCounter;
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
        exprStmt.expr.accept(this);
    }

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.cond.accept(this);
        cgen.genPop(T0);
        String falseLabel = cgen.nextLabel();
        String endLabel = cgen.nextLabel();
        cgen.generate("beq", T0, R0, falseLabel);
        ifStmt.stmt.accept(this);
        cgen.generate("j", endLabel);
        cgen.genLabel(falseLabel);
        if (ifStmt.elseStmt != null) ifStmt.elseStmt.accept(this);
        cgen.genLabel(endLabel);
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
        FunctionDecl functionDecl = (FunctionDecl) Scope.getInstance().getContext(Context.FUNCTION);
        if (returnStmt.expr != null) {
            returnStmt.expr.accept(this);
            cgen.genPop(V0);
        }
        cgen.generate("j", "_" + functionDecl.id + "_Exit");
    }

    @Override
    public void visit(Stmt.PrintStmt printStmt) {
        for (Expr expr : printStmt.exprs) {
            expr.accept(this);
            cgen.genPop(A0);
            Type exprType = expr.getType();
            String v0 = null;
            if (exprType instanceof Type.PrimitiveType.IntegerType || exprType instanceof Type.PrimitiveType.BooleanType)
                v0 = "1";
            else if (exprType instanceof Type.PrimitiveType.StringType) {
                v0 = "4";
            }
            cgen.generate("li", V0, v0);
            cgen.generate("syscall");
        }
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
        lValue.accept(this);
    }

    @Override
    public void visit(SimpleLVal lValue) {
        cgen.generateIndexed("lw", A0, FP, lValue.getOffset());
        cgen.genPush(A0);
    }

    @Override
    public void visit(Expr.ConstExpr constExpr) {
       constExpr.constant.accept(this);
    }

    @Override
    public void visit(Expr.ThisExpr thisExpr) {

    }

    @Override
    public void visit(Expr.CallExpr callExpr) {
        callExpr.call.accept(this);
    }


    @Override
    public void visit(AssignExpr assignExpr) {
        assignExpr.rightHandSide.accept(this);
        cgen.genPop(A0);
        cgen.generateIndexed("sw", A0, FP, assignExpr.leftHandSide.getOffset());
    }

    @Override
    public void visit(Expr.LValExpr lValExpr) {
        lValExpr.lValue.accept(this);
    }


    @Override
    public void visit(Expr.BinOpExpr.AddExpr addExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr arithExpr) {

    }

    public void generateCompExpr(String opcode) {
        cgen.genPop(T1);
        cgen.genPop(T0);
        String lTrue = cgen.nextLabel();
        String lEnd = cgen.nextLabel();
        cgen.generate(opcode, T0, T1, lTrue);
        cgen.generate("li", A0, "0");
        cgen.generate("j", lEnd);
        cgen.genLabel(lTrue);
        cgen.generate("li", A0, "1");
        cgen.genLabel(lEnd);
        cgen.genPush(A0);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr compExpr) {
        compExpr.expr1.accept(this);
        compExpr.expr2.accept(this);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.LessExpr lessExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) lessExpr);
        generateCompExpr("blt");
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.LessEqExpr lessEqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) lessEqExpr);
        generateCompExpr("ble");

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.GreaterExpr greaterExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) greaterExpr);
        generateCompExpr("bgt");
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.GreaterEqExpr greaterEqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) greaterEqExpr);
        generateCompExpr("bge");

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.EqExpr eqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) eqExpr);
        generateCompExpr("beq");
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.NotEqExpr notEqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) notEqExpr);
        generateCompExpr("bne");
    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr logicalExpr) {

    }

    @Override
    public void visit(Call.SimpleCall simpleCall) {
        for (Expr actual : simpleCall.actuals) {
            actual.accept(this);
        }
        cgen.generate("j", "_" + simpleCall.id);
    }

    @Override
    public void visit(Call.DottedCall dottedCall) {

    }

    @Override
    public void visit(Constant.IntConst intConst) {
        cgen.generate("li", A0, String.valueOf(intConst.value));
        cgen.genPush(A0);
    }

    @Override
    public void visit(Constant.BoolConst boolConst) {
        cgen.generate("li", A0, boolConst.value ? "1" : "0");
        cgen.genPush(A0);
    }

}

package compiler.visitors;

import compiler.AST.*;
import compiler.AST.Constant.StringConst;
import compiler.AST.Decl.FunctionDecl;
import compiler.AST.Decl.VariableDecl;
import compiler.AST.Expr.AssignExpr;
import compiler.AST.Expr.FunctionExpr.BtoiExpr;
import compiler.AST.Expr.FunctionExpr.DtoiExpr;
import compiler.AST.Expr.FunctionExpr.ItobExpr;
import compiler.AST.Expr.FunctionExpr.ItodExpr;
import compiler.AST.Expr.FunctionExpr.ReadIntExpr;
import compiler.AST.Expr.FunctionExpr.ReadLineExpr;
import compiler.AST.Expr.InitExpr.ArrInit;
import compiler.AST.LValue.IndexedLVal;
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

    public static final int CHAR_SIZE = CodeGenerator.CHAR_SIZE; // in bytes
    public static final int BUFFER_SIZE = CodeGenerator.BUFFER_SIZE;

    @Override
    public void visit(Program program) {
        generateGlobalVars(program.getGlobalVars());
        for (FunctionDecl functionDecl : program.getFunctionDecls()) {
            if (functionDecl.id.equals("main")) {
                cgen.text.append(".text\n")
                        .append(".globl main\n");
                cgen.genLabel("main");
                generateFunction(functionDecl);
            }
            else functionDecl.accept(this);
        }
    }

    public void generateGlobalVars(List<Variable> globalVars) {
        for (Variable globalVar : globalVars) {
            cgen.text.append("\t.data\n")
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
        boolean returnsValue = !functionDecl.returnType.isLessThan(Type.voidType());
        this.generateFunctionEntry(paramSize, localsSize);
        functionDecl.stmtBlock.accept(this);
        this.generateFunctionExit(functionDecl.id, paramSize, returnsValue);
        Scope.popScope();
    }

    public void generateFunctionEntry(int paramSize, int localsSize) {
        cgen.genPush(RA);
        cgen.genPush(FP);
        cgen.generate("addu", FP, SP, String.valueOf(paramSize + 8));
        cgen.generate("subu", SP, SP, String.valueOf(localsSize));
    }

    public void generateFunctionExit(String functionName, int paramSize, boolean returnsValue) {
        cgen.genLabel("_" + functionName + "_Exit");
        cgen.generateIndexed("lw", RA, FP, -paramSize);
        cgen.generate("move", T0, FP);
        cgen.generateIndexed("lw", FP, FP, -(paramSize + 4));
        cgen.generate("move", SP, T0);
        if (returnsValue) cgen.genPush(V0);
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
            if (returnStmt.expr.getType() instanceof Type.PrimitiveType.DoubleType)
                cgen.genPop(FV0);
            else
                cgen.genPop(V0);
        }
        cgen.generate("j", "_" + functionDecl.id + "_Exit");
    }

    @Override
    public void visit(Stmt.PrintStmt printStmt) {
        for (Expr expr : printStmt.exprs) {
            expr.accept(this);
            Type exprType = expr.getType();
            String v0 = null;
            if (exprType instanceof Type.PrimitiveType.IntegerType || 
                exprType instanceof Type.PrimitiveType.BooleanType) {
                cgen.genPop(A0);
                v0 = "1";
                }
            else if (exprType instanceof Type.PrimitiveType.StringType) {
                cgen.genPop(A0);
                v0 = "4";
            }
            else if (exprType instanceof Type.PrimitiveType.DoubleType){
                cgen.genPop(FA0);
                v0 = "2";
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
    public void visit(SimpleLVal lValue) {
        if (lValue.getType() instanceof Type.PrimitiveType.DoubleType) {
            cgen.generateIndexed("lwc1", FA0, FP, lValue.getOffset());
            cgen.genPush(FA0);
        } else {
            cgen.generateIndexed("lw", A0, FP, lValue.getOffset());
            cgen.genPush(A0);
        }
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

        System.out.println(assignExpr.rightHandSide.getType());
        System.out.println(assignExpr.leftHandSide.getType());

        if (assignExpr.rightHandSide.getType() instanceof Type.PrimitiveType.DoubleType) {
            cgen.genPop(FA0);
            cgen.generateIndexed("swc1", FA0, FP, assignExpr.leftHandSide.getOffset());
        } else {
            cgen.genPop(A0);
            cgen.generateIndexed("sw", A0, FP, assignExpr.leftHandSide.getOffset());
        }
    }

    @Override
    public void visit(Expr.LValExpr lValExpr) {
        lValExpr.lValue.accept(this);
    }

    @Override
    public void visit(Expr.BinOpExpr binOpExpr) {
        binOpExpr.expr1.accept(this);
        binOpExpr.expr2.accept(this);
        if (binOpExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType) {
            cgen.genPop(FT1);
            cgen.genPop(FT0);
        } else {
            cgen.genPop(T1);
            cgen.genPop(T0);
        }
    }


    @Override
    public void visit(Expr.BinOpExpr.AddExpr addExpr) {
        this.visit((Expr.BinOpExpr) addExpr);
        Expr.BinOpExpr.AddExpr castedExpr = addExpr.downcast();
        castedExpr.accept(this);
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.IntAddExpr intAddExpr) {
        generateArithExpr(intAddExpr, "add");
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.DoubleAddExpr doubleAddExpr) {
        generateArithExpr(doubleAddExpr, "add.s");
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.StringAddExpr stringAddExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.ArrayAddExpr arrayAddExpr) {

    }

    public void generateArithExpr(Expr.BinOpExpr binOpExpr, String opcode) {
        this.visit(binOpExpr);
        if (binOpExpr.getType() instanceof Type.PrimitiveType.DoubleType) {
            cgen.generate(opcode, FA0, FT0, FT1);
            cgen.genPush(FA0);
        } else {
            cgen.generate(opcode, A0, T0, T1);
            cgen.genPush(A0);
        }
    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr arithExpr) {
    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.SubExpr subExpr) {
        String opcode = "sub";
        if (subExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType )
            opcode += ".s";
        this.generateArithExpr(subExpr, opcode);
    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.MultExpr multExpr) {
        String opcode = "mul";
        if (multExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType )
            opcode+= ".s";
        generateArithExpr(multExpr, opcode);
    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.DivExpr divExpr) {
        this.visit((Expr.BinOpExpr) divExpr);
        if (divExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType){
            cgen.generate("div.s", FS0, FT0, FT1);
            cgen.genPush(FS0);
        } else {
            cgen.generate("div", T0, T1);
            cgen.generate("mflo", A0);
            cgen.genPush(A0);
        }
    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.ModExpr modExpr) {
        // TODO : FOR FP
        this.visit((Expr.BinOpExpr) modExpr);
        cgen.generate("div", T0, T1);
        cgen.generate("mfhi", A0);
        cgen.genPush(A0);
    }

    public void generateFloatingPointCompExpr(Expr.BinOpExpr binOpExpr, String opcode, boolean reversed) {
        this.visit(binOpExpr);
        String lCond = cgen.nextLabel();
        String lEnd = cgen.nextLabel();
        cgen.generate(opcode, FT0, FT1);
        String conditionalBranchOpcode = reversed ? "bc1f" : "bc1t";
        cgen.generate(conditionalBranchOpcode, lCond);
        cgen.generate("li", A0, "0");
        cgen.generate("j", lEnd);
        cgen.genLabel(lCond);
        cgen.generate("li", A0, "1");
        cgen.genLabel(lEnd);
        cgen.genPush(A0);
    }

    public void generateCompExpr(Expr.BinOpExpr binOpExpr, String opcode) {
        this.visit(binOpExpr);
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

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.LessExpr lessExpr) {
        if (lessExpr.expr1.getType() instanceof Type.PrimitiveType.IntegerType)
            generateCompExpr(lessExpr, "blt");
        else if (lessExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType)
            generateFloatingPointCompExpr(lessExpr, "c.lt.s", false);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.LessEqExpr lessEqExpr) {
        if (lessEqExpr.expr1.getType() instanceof Type.PrimitiveType.IntegerType)
            generateCompExpr(lessEqExpr, "ble");
        else if (lessEqExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType)
            generateFloatingPointCompExpr(lessEqExpr, "c.le.s", false);

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.GreaterExpr greaterExpr) {
        if (greaterExpr.expr1.getType() instanceof Type.PrimitiveType.IntegerType)
            generateCompExpr(greaterExpr, "bgt");
        else if (greaterExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType)
            generateFloatingPointCompExpr(greaterExpr, "c.le.s", true);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.GreaterEqExpr greaterEqExpr) {
        if (greaterEqExpr.expr1.getType() instanceof Type.PrimitiveType.IntegerType)
            generateCompExpr(greaterEqExpr, "bge");
        else if (greaterEqExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType)
            generateFloatingPointCompExpr(greaterEqExpr, "c.lt.s", true);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.EqExpr eqExpr) {
        if (eqExpr.expr1.getType() instanceof Type.PrimitiveType.IntegerType)
            generateCompExpr(eqExpr, "beq");
        else if (eqExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType)
            generateFloatingPointCompExpr(eqExpr, "c.eq.s", false);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.NotEqExpr notEqExpr) {
        if (notEqExpr.expr1.getType() instanceof Type.PrimitiveType.IntegerType)
            generateCompExpr(notEqExpr, "bne");
        else if (notEqExpr.expr1.getType() instanceof Type.PrimitiveType.DoubleType)
            generateFloatingPointCompExpr(notEqExpr, "c.eq.s", true);
    }

    public void generateLogicalExpr(Expr.BinOpExpr binOpExpr, String opcode) {
        this.visit(binOpExpr);
        cgen.generate(opcode, A0, T0, T1);
        cgen.genPush(A0);
    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr logicalExpr) {
    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr.AndExpr andExpr) {
        generateLogicalExpr(andExpr, "and");
    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr.OrExpr orExpr) {
        generateLogicalExpr(orExpr, "or");
    }

    @Override
    public void visit(Expr.UnOpExpr.ArithExpr.MinusExpr minusExpr) {
        minusExpr.expr.accept(this);
        cgen.genPop(A0);
        cgen.generate("nor", T0, A0, A0);
        cgen.generate("addi", T0, T0, "1");
        cgen.genPush(T0);
    }

    @Override
    public void visit(Expr.UnOpExpr.LogicalExpr.NotExpr notExpr) {
        notExpr.expr.accept(this);
        cgen.genPop(A0);
        cgen.generate("nor", T0, A0, A0);
        cgen.genPush(T0);
    }

    @Override
    public void visit(Call.SimpleCall simpleCall) {
        for (Expr actual : simpleCall.actuals) {
            actual.accept(this);
        }
        cgen.generate("jal", "_" + simpleCall.id);
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

    @Override
    public void visit(Constant.DoubleConst doubleConst) {
        cgen.generate("li.s", FA0, String.valueOf(doubleConst.value));
        cgen.genPush(FA0);
    }

    @Override
    public void visit(StringConst stringConst) {
        String stringValue = stringConst.value;
        int stringLength = stringValue.length();
        String ptrLabel = cgen.malloc((stringLength + 1) * CHAR_SIZE);
        cgen.generate("lw", T0, ptrLabel);
        for (char c : stringValue.toCharArray()) {
            System.out.println(String.valueOf(Character.getNumericValue(c)));
            cgen.generate("li", T1, String.valueOf((int) c));
            cgen.generate("sb", T1, "(" + T0 + ")");
            cgen.generate("addi", T0, String.valueOf(CHAR_SIZE));
        }
        cgen.generate("sb", R0, "(" + T0 + ")");
        cgen.generate("lw", A0, ptrLabel);
        cgen.genPush(A0);
    }

    @Override
    public void visit(ReadIntExpr readIntExpr) {
        cgen.generate("li", V0, "5");
        cgen.generate("syscall");
        cgen.genPush(V0);
    }

    @Override
    public void visit(ReadLineExpr readLineExpr) {
        String ptrLabel = cgen.malloc(BUFFER_SIZE);
        cgen.generateWithComment("la", "read string" ,A0, ptrLabel);
        cgen.generate("li", A1, String.valueOf(BUFFER_SIZE));
        cgen.generate("li", V0, "8");
        cgen.genEmptyLine();
        cgen.generateWithComment("move", "save buffer pointer" , S0, A0);
        cgen.generate("syscall");
        cgen.generate("la", A0, ptrLabel);
        cgen.genEmptyLine();
        cgen.genPush(A0);
    }

    

    @Override
    public void visit(ItodExpr itodExpr) {
        
    }

    @Override
    public void visit(DtoiExpr dtoiExpr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ItobExpr itobExpr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(BtoiExpr btoiExpr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ArrInit arrInit) {
        arrInit.expr.accept(this);
        cgen.genPop(T0);
        cgen.generate("li", T1, "4");
        cgen.generate("mul", A0, T0, T1);
        String ptrLabel = cgen.malloc();
        cgen.generate("lw", A0, ptrLabel);
        cgen.genPush(A0);
    }

    @Override
    public void visit(IndexedLVal indexedLVal) {
        indexedLVal.expr.accept(this);
        indexedLVal.index.accept(this);
        cgen.genPop(T1);
        cgen.genPop(T0);
        cgen.generate("li", T2, String.valueOf(indexedLVal.expr.getType().getSize()));
        cgen.generate("mult", T1, T2, T3);
        cgen.generate("add", A0, T0, T3);
        cgen.genPush(A0);
    }


}

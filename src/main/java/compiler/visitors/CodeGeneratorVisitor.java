package compiler.visitors;

import compiler.AST.*;
import compiler.AST.Constant.StringConst;
import compiler.AST.Decl.FunctionDecl;
import compiler.AST.Decl.VariableDecl;
import compiler.AST.Expr.AssignExpr;
import compiler.AST.Expr.LValExpr;
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
import compiler.models.Loop;
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
        generateClasses(program.getClassDecls());
        generateGlobalFunctions(program.getFunctionDecls());
    }

    public void generateGlobalFunctions(List<FunctionDecl> functionDecls) {
        for (FunctionDecl functionDecl : functionDecls) {
            if (functionDecl.isMain) {
                cgen.text.append(".globl main\n");
                functionDecl.setLabel("main");
                generateFunction(functionDecl, 0);
            }
            else functionDecl.accept(this);
        }
    }

    public void generateFunction(FunctionDecl funcDecl, int paramSize) {
        Scope.pushScope(funcDecl);
        cgen.genLabel(funcDecl.getLabel());
        for (Variable formal : funcDecl.formals) {
            Decl.variableDecl(formal).accept(this);
        }
        funcDecl.setOffsetCounter(funcDecl.getOffsetCounter() - 8);
        generateFunctionEntry(paramSize, funcDecl.stmtBlock.getRequiredSpace());
        generateFunctionBody(funcDecl);
        generateFunctionExit(funcDecl, paramSize);
        Scope.popScope();
    }

    public void generateClasses(List<Decl.ClassDecl> classDecls) {
        for (Decl.ClassDecl classDecl : classDecls) {
            Scope.pushScope(classDecl);
            classDecl.accept(this);
            Scope.popScope();
        }

        cgen.text.append("InitClasses:\n");
        for (Decl.ClassDecl classDecl : classDecls) {
            initVTable(classDecl);
        }
        cgen.generate("jr", RA);
        cgen.generateEmptyLine();
    }

    public void initVTable(Decl.ClassDecl classDecl) {
        String vTablePtr = cgen.malloc(classDecl.getRequiredSpaceForMethods());
        cgen.generate("la", A0, vTablePtr);
        classDecl.setvTablePtr(vTablePtr);
        List<ClassField.MethodField> inheritedMethods = classDecl.getInheritedMethods();
        for (ClassField.MethodField inheritedMethod : inheritedMethods) {
            cgen.generate("la", T0, ((FunctionDecl) inheritedMethod.decl).getLabel());
            cgen.generateIndexed("sw", T0, A0, inheritedMethod.getOffset());
        }
    }

    public void generateGlobalVars(List<VariableDecl> globalVars) {
        for (VariableDecl globalVar : globalVars) {
            cgen.data.append("\t.align 2\n")
                    .append("_").append(globalVar.id).append(": ").append(".space ").append(globalVar.variable.type.getSize()).append("\n");
        }
    }

    public void generateFunctionEntry(int paramSize, int localsSize) {
        cgen.genPush(RA);
        cgen.genPush(FP);
        cgen.generate("addu", FP, SP, String.valueOf(paramSize + 8));
        cgen.generate("subu", SP, SP, String.valueOf(localsSize));
    }

    public void generateFunctionBody(FunctionDecl funcDecl) {
        if (funcDecl.isMain) cgen.generate("jal", "InitClasses");
        funcDecl.stmtBlock.accept(this);
        cgen.generateEmptyLine();
    }

    public void generateFunctionExit(FunctionDecl funcDecl, int paramSize) {
        cgen.genLabel(funcDecl.getLabel() + "_Exit");
        cgen.generateIndexed("lw", RA, FP, -paramSize);
        cgen.generate("move", T0, FP);
        cgen.generateIndexed("lw", FP, FP, -(paramSize + 4));
        cgen.generate("move", SP, T0);
        if (!funcDecl.returnType.isLessThan(Type.voidType())) cgen.genPush(V0);
        cgen.generate("jr", RA);
        cgen.generateEmptyLine();
    }

    @Override
    public void visit(VariableDecl variableDecl) {
        FunctionDecl functionDecl = (FunctionDecl) Scope.getInstance().getContext(Context.FUNCTION);
        int offsetCounter = functionDecl.getOffsetCounter();
        variableDecl.setOffset(offsetCounter);
        Scope.getInstance().setEntry(variableDecl.id, variableDecl);
        offsetCounter -= variableDecl.getType().getSize();
        functionDecl.setOffsetCounter(offsetCounter);
    }

    @Override
    public void visit(FunctionDecl functionDecl) {
        functionDecl.setLabel("_" + functionDecl.id);
        generateFunction(functionDecl, functionDecl.getSizeofParameters(false));
    }

    @Override
    public void visit(Decl.ClassDecl classDecl) {
        for (ClassField.MethodField methodField : classDecl.getMethodFields()) {
            String methodLabel = "_" + classDecl.id + "_" + methodField.id;
            FunctionDecl functionDecl = (FunctionDecl) methodField.decl;
            functionDecl.setLabel(methodLabel);
            generateFunction(functionDecl, functionDecl.getSizeofParameters( true));
        }
    }

    @Override
    public void visit(ClassField.VarField varField) {

    }

    @Override
    public void visit(ClassField.MethodField methodField) {

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
        Scope.pushScope(whileStmt);
        String startLabel = "cont_" + whileStmt.getLabel();
        cgen.genLabel(startLabel);
        whileStmt.cond.accept(this);
        cgen.genPop(A0);
        String endLabel = "end_" + whileStmt.getLabel();
        cgen.generate("beq", A0, R0, endLabel);
        whileStmt.stmt.accept(this);
        cgen.generate("j", startLabel);
        cgen.genLabel(endLabel);
        Scope.popScope();
    }

    @Override
    public void visit(ForStmt forStmt) {
        Scope.pushScope(forStmt);
        forStmt.init.accept(this);
        String startLabel = cgen.nextLabel();
        cgen.genLabel(startLabel);
        forStmt.cond.accept(this);
        cgen.genPop(A0);
        String endLabel = "end_" + forStmt.getLabel();
        cgen.generate("beq", A0, R0, endLabel);
        forStmt.stmt.accept(this);
        String contLabel = "cont_" + forStmt.getLabel();
        cgen.genLabel(contLabel);
        forStmt.update.accept(this);
        cgen.generate("j", startLabel);
        cgen.genLabel(endLabel);
        Scope.popScope();
    }

    @Override
    public void visit(BreakStmt breakStmt) {
        Loop loop = (Loop) Scope.getInstance().getContext(Context.LOOP);
        String endLabel = "end_" + loop.getLabel();
        cgen.generate("j", endLabel);
    }

    @Override
    public void visit(Stmt.ContinueStmt continueStmt) {
        Loop loop = (Loop) Scope.getInstance().getContext(Context.LOOP);
        String contLabel = "cont_" + loop.getLabel();
        cgen.generate("j", contLabel);
    }

    @Override
    public void visit(Stmt.ReturnStmt returnStmt) {
        FunctionDecl functionDecl = (FunctionDecl) Scope.getInstance().getContext(Context.FUNCTION);
        String exitLabel = functionDecl.getLabel() + "_" + "Exit";
        if (returnStmt.expr != null) {
            returnStmt.expr.accept(this);
            if (returnStmt.expr.getType() instanceof Type.PrimitiveType.DoubleType)
                cgen.genPop(FV0);
            else
                cgen.genPop(V0);
        }
        cgen.generate("j", exitLabel);
    }

    @Override
    public void visit(Stmt.PrintStmt printStmt) {
        for (Expr expr : printStmt.exprs) {
            expr.accept(this);
            Type exprType = expr.getType();
            String v0 = null;
            if (exprType instanceof Type.PrimitiveType.IntegerType) {
                cgen.genPop(A0);
                v0 = "1";
            } else if (exprType instanceof Type.PrimitiveType.BooleanType) {
                cgen.genPop(T0);
                String falseLabel = cgen.nextLabel();
                String endLabel = cgen.nextLabel();
                cgen.generate("beq", T0, R0, falseLabel);
                cgen.generate("la", A0, "true");
                cgen.generate("j", endLabel);
                cgen.genLabel(falseLabel);
                cgen.generate("la", A0, "false");
                cgen.genLabel(endLabel);
                v0 = "4";
            } else if (exprType instanceof Type.PrimitiveType.StringType) {
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
        cgen.generate("li", V0, "4");
        cgen.generate("la", A0, "newline");
        cgen.generate("syscall");
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
    public void visit(Expr.ConstExpr constExpr) {
       constExpr.constant.accept(this);
    }

    @Override
    public void visit(Expr.ThisExpr thisExpr) {
        cgen.generateIndexed("lw", A0, FP, 0);
        cgen.genPush(A0);
    }

    @Override
    public void visit(Expr.CallExpr callExpr) {
        callExpr.call.accept(this);
    }

    public void setLhsAddress(LValue lhs) {
        cgen.generateOneLineComment("setting lvalue offset");
        if (lhs instanceof IndexedLVal) {
            IndexedLVal indexedLVal = (IndexedLVal) lhs;
            indexedLVal.index.accept(this);
            setLhsAddress(((LValExpr)indexedLVal.expr).lValue);
            cgen.genPop(A0);
            cgen.genPop(A1);
            cgen.generateIndexed("lw", T0, A0, 0);
            cgen.generate("li", T1, "4");
            cgen.generate("mul", T2, T1, A1);
            cgen.generate("add", A2, T2, T0);
            cgen.genPush(A2);
        } else if (lhs instanceof SimpleLVal) {
            VariableDecl variableDecl = (VariableDecl) Scope.getInstance().getEntry(((SimpleLVal) lhs).id);
            if (variableDecl.isGlobal) {
                cgen.generate("la", A0, "_"  + variableDecl.id);
                cgen.genPush(A0);
            } else {
                cgen.generate("li", T2, String.valueOf(lhs.getOffset()));
                cgen.generate("add", S0, T2, FP);
                cgen.genPush(S0);
            }
        } else if (lhs instanceof LValue.DottedLVal) {
            LValue.DottedLVal dottedLVal = (LValue.DottedLVal) lhs;
            dottedLVal.expr.accept(this);
            cgen.genPop(A0);
            int offset = dottedLVal.getOffset();
            cgen.generate("li", T0, String.valueOf(offset));
            cgen.generate("add", A0, A0, T0);
            cgen.genPush(A0);
        }
        cgen.generateEmptyLine();
    }


    @Override
    public void visit(AssignExpr assignExpr) {
        cgen.generateOneLineComment("Assign expr");
        setLhsAddress(assignExpr.leftHandSide);
        assignExpr.rightHandSide.accept(this);
        cgen.genPop(T1);
        cgen.genPop(T0);
        if (assignExpr.rightHandSide.getType() instanceof Type.PrimitiveType.DoubleType) {
            cgen.generate("mtc1", T1, FA0);
            cgen.generateIndexed("swc1", FA0, T0, 0);
            cgen.genPush(FA0);
        } else {
            cgen.generateIndexed("sw", T1, T0, 0);
            cgen.genPush(T1);
        }
    }

    @Override
    public void visit(Expr.LValExpr lValExpr) {
        lValExpr.lValue.accept(this);
    }

    @Override
    public void visit(SimpleLVal lValue) {
        VariableDecl variableDecl = (VariableDecl) Scope.getInstance().getEntry(lValue.id);
        cgen.generateOneLineComment("Calculate SimpleLVal");
        if (lValue.getType() instanceof Type.PrimitiveType.DoubleType) {
            if (variableDecl.isGlobal) cgen.generate("lwc1", FA0, "_" + variableDecl.id);
            else cgen.generateIndexed("lwc1", FA0, FP, lValue.getOffset());
            cgen.genPush(FA0);
        } else {
            if (variableDecl.isGlobal)
                cgen.generate("lw", A0, "_" + variableDecl.id);
            else cgen.generateIndexed("lw", A0, FP, lValue.getOffset());
            cgen.genPush(A0);
        }
        cgen.generateEmptyLine();
    }

    @Override
    public void visit(IndexedLVal indexedLVal) {
        setLhsAddress(indexedLVal);
        cgen.genPop(T0);
        cgen.generateOneLineComment("Calculate indexedVal");
        cgen.generateIndexed("lw", T1, T0, 0);
        cgen.generateEmptyLine();
        cgen.genPush(T1);
    }

    @Override
    public void visit(LValue.DottedLVal dottedLVal) {
        dottedLVal.expr.accept(this);
        cgen.genPop(A0);
        cgen.generateIndexed("lw", T0, A0, dottedLVal.getOffset());
        cgen.genPush(T0);
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
        if (eqExpr.expr1.getType() instanceof Type.PrimitiveType.IntegerType ||
                eqExpr.expr1.getType() instanceof Type.PrimitiveType.BooleanType)
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
        if (minusExpr.expr.getType() instanceof Type.PrimitiveType.IntegerType) {
            cgen.generate("nor", T0, A0, A0);
            cgen.generate("addi", T0, T0, "1");
            cgen.genPush(T0);
        } else  {
            cgen.generate("mtc1", A0, FA0);
            cgen.generate("neg.s", FA0, FA0);
            cgen.genPush(FA0);
        }
    }

    @Override
    public void visit(Expr.UnOpExpr.LogicalExpr.NotExpr notExpr) {
        notExpr.expr.accept(this);
        cgen.genPop(A0);
        cgen.generate("nor", T0, A0, A0);
        cgen.generate("andi", T0, T0, "1");
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
        dottedCall.expr.accept(this); // "this" is on top of stack
        for (Expr actual : dottedCall.actuals) {
            actual.accept(this);
        }
        Decl.ClassDecl classDecl = dottedCall.getClassDecl();
        cgen.generate("la", A0, classDecl.getvTablePtr());
        int offset = classDecl.getFieldMap().get(dottedCall.id).getOffset();
        cgen.generateIndexed("lw", T0, A0, offset);
        cgen.generate("jalr", T0);
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
        cgen.generateEmptyLine();
        cgen.generateWithComment("move", "save buffer pointer" , S0, A0);
        cgen.generate("syscall");
        cgen.generate("la", A0, ptrLabel);
        cgen.generateEmptyLine();
        cgen.genPush(A0);
    }

    

    @Override
    public void visit(ItodExpr itodExpr) {
        itodExpr.expr.accept(this);
        cgen.genPop(A0);
        cgen.generate("mtc1", A0, FA0);
        cgen.generate("cvt.s.w", FA0, FA0);
        cgen.genPush(FA0);
    }

    @Override
    public void visit(DtoiExpr dtoiExpr) {
        dtoiExpr.expr.accept(this);
        cgen.genPop(A0);
        cgen.generate("mtc1", A0, FA0);
        cgen.generate("cvt.w.s", FA0, FA0);
        cgen.genPush(FA0);
    }

    @Override
    public void visit(ItobExpr itobExpr) {
        itobExpr.expr.accept(this);
        cgen.genPop(A0);
        cgen.generate("slt", T0, R0, A0);
        cgen.genPush(T0);
    }

    @Override
    public void visit(BtoiExpr btoiExpr) {
        btoiExpr.expr.accept(this);
    }

    public void createArray() {
        cgen.genPop(A0);
        cgen.generate("move", T0, A0);
        cgen.generate("addi", T1, T0, "1");
        cgen.generate("li", T2, "4");
        cgen.generate("mul", T3, T1, T2);
        cgen.generate("move", A0, T3);
        String ptr = cgen.malloc();
        cgen.generate("lw", T4, ptr);
        cgen.generateIndexed("sw", T0, T4, 0);
        cgen.generate("addi", V0, T4, "4");
        cgen.genPush(V0);
    }

    @Override
    public void visit(ArrInit arrInit) {
        arrInit.expr.accept(this);
        createArray();
    }

    @Override
    public void visit(Expr.InitExpr.ObjInit objInit) {
        Decl.ClassDecl classDecl = (Decl.ClassDecl) Scope.getInstance().getEntry(objInit.id);
        String ptrLabel = cgen.malloc(classDecl.getRequiredSpaceForVars());
        cgen.generate("la", A0, ptrLabel);
        cgen.genPush(A0);
    }

    // subroutine, doesnt use saved regs
    public void calcStringLen() {
        cgen.genPop(A0); // get string ptr
        cgen.generate("move", T0, A0); // ptr to current char
        cgen.generate("li", T1, "0"); // counter
        String loopLabel = cgen.nextLabel();
        String endLabel = cgen.nextLabel();
        cgen.genLabel(loopLabel);
        cgen.generate("move", T2, R0); // set T2 to 0
        cgen.generateIndexed("lb", T2, T0, 0); // load current char to T2
        cgen.generate("beq", T2, R0, endLabel);
        cgen.generate("addi", T0, "1");
        cgen.generate("addi", T1, "1");
        cgen.generate("j", loopLabel);
        cgen.genLabel(endLabel);
        cgen.generate("move", V0, T1);
        cgen.genPush(V0);
    }

    public void createString() {
        cgen.genPop(A0);
        cgen.generate("addi", A0, A0, "1");
        String ptr = cgen.malloc();
        cgen.generate("lw", V0, ptr);
        cgen.genPush(V0);
    }
    

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.StringAddExpr stringAddExpr) {
        stringAddExpr.expr1.accept(this);
        stringAddExpr.expr2.accept(this);

        cgen.genPop(A1); // ptr y
        cgen.genPop(A0); // ptr x
        cgen.generate("move", S1, A1); // ptr y
        cgen.generate("move", S0, A0); // ptr x
        cgen.genPush(S0);
        calcStringLen();
        cgen.genPop(S3); // len x
        cgen.genPush(S1);
        calcStringLen();
        cgen.genPop(S4); // len y
        cgen.generate("add", S5, S3, S4); // len z
        cgen.genPush(S5);
        createString();
        cgen.genPop(S2); // ptr z
        cgen.generate("move", A0, S2);
        cgen.generate("li", A1, "0");
        cgen.generate("addi", T0, S5, "1");
        cgen.generate("move", A2, T0);
        cgen.memSetBytes(); // set string z to 0
        // ptr x y z len x y z : S0 S1 S2 S3 S4 S5

        // move ptr z to A0 (dest), ptr x to A1 (src), len x to A2 (#words)
        cgen.generate("move", A0, S2);
        cgen.generate("move", A1, S0);
        cgen.generate("move", A2, S3);
        cgen.memCpyBytes();

        // move ptr z + len x to A0 (dest), ptr y to A1 (src), len y to A2 (#words)
        cgen.generate("add", T2, S2, S3 );
        cgen.generate("move", A0, T2);
        cgen.generate("move", A1, S1);
        cgen.generate("move", A2, S4);
        cgen.memCpyBytes();

        // return ptr z
        cgen.genPush(S2);
    }

    // subroutines dont use $s registers
    public void calcArrayLen() {
        cgen.genPop(A0); // get array ptr
        cgen.generate("addi", A0, A0, "-4");
        cgen.generateIndexed("lw", T0, A0, 0);
        cgen.genPush(T0);
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.ArrayAddExpr arrayAddExpr) {
        arrayAddExpr.expr1.accept(this);
        arrayAddExpr.expr2.accept(this);

        cgen.genPop(A1); // ptr y
        cgen.genPop(A0); // ptr x
        cgen.generate("move", S1, A1); // ptr y
        cgen.generate("move", S0, A0); // ptr x
        cgen.genPush(S0);
        calcArrayLen();
        cgen.genPop(S3); // len x
        cgen.genPush(S1);
        calcArrayLen();
        cgen.genPop(S4); // len y
        cgen.generate("add", S5, S3, S4); // len z
        cgen.genPush(S5);
        createArray();
        cgen.genPop(S2); // ptr z
        // ptr x y z len x y z : S0 S1 S2 S3 S4 S5

        // move ptr z to A0 (dest), ptr x to A1 (src), len x to A2 (#words)
        cgen.generate("move", A0, S2);
        cgen.generate("move", A1, S0);
        cgen.generate("move", A2, S3);
        cgen.memCpyWords();

        // move ptr z + len x * 4 to A0 (dest), ptr y to A1 (src), len y to A2 (#words)
        cgen.generate("li", T0, "4" );
        cgen.generate("mul", T1, S3, "4" );
        cgen.generate("add", T2, S2, T1 );
        cgen.generate("move", A0, T2);
        cgen.generate("move", A1, S1);
        cgen.generate("move", A2, S4);
        cgen.memCpyWords();

        // return ptr z
        cgen.genPush(S2);
    }


}

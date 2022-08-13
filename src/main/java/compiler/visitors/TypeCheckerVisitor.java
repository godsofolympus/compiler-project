package compiler.visitors;

import compiler.AST.*;
import compiler.AST.Constant.DoubleConst;
import compiler.AST.Constant.StringConst;
import compiler.AST.Expr.FunctionExpr.BtoiExpr;
import compiler.AST.Expr.FunctionExpr.DtoiExpr;
import compiler.AST.Expr.FunctionExpr.ItobExpr;
import compiler.AST.Expr.FunctionExpr.ItodExpr;
import compiler.AST.Expr.FunctionExpr.ReadIntExpr;
import compiler.AST.Expr.FunctionExpr.ReadLineExpr;
import compiler.AST.Expr.InitExpr.ArrInit;
import compiler.AST.LValue.IndexedLVal;
import compiler.Exceptions.*;
import compiler.models.Context;
import compiler.models.ContextualScoped;
import compiler.models.Scope;

import java.util.Arrays;
import java.util.List;

public class TypeCheckerVisitor implements Visitor{

    @Override
    public void visit(Program program) {
        for (Decl decl : program.decls) {
            Scope.getInstance().setEntry(decl.id, decl);
        }
        try {
            Scope.getInstance().getEntry("main");
        }
        catch (SymbolNotFoundException e) {
            throw new MainMethodNotFoundException();
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
    public void visit(Stmt.ExprStmt exprStmt) {
        exprStmt.expr.accept(this);
    }

    @Override
    public void visit(Stmt.IfStmt ifStmt) {
        ifStmt.cond.accept(this);
        if (!ifStmt.cond.getType().isLessThan(Type.PrimitiveType.booleanType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.booleanType(), ifStmt.cond.getType());
        ifStmt.stmt.accept(this);
        if (ifStmt.elseStmt != null) ifStmt.elseStmt.accept(this);
    }

    @Override
    public void visit(Stmt.WhileStmt whileStmt) {
        whileStmt.cond.accept(this);
        if (!whileStmt.cond.getType().isLessThan(Type.PrimitiveType.booleanType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.booleanType(), whileStmt.cond.getType());
        Scope.pushScope(whileStmt);
        whileStmt.stmt.accept(this);
        Scope.popScope();
    }

    @Override
    public void visit(Stmt.ForStmt forStmt) {
        if (forStmt.init != null) forStmt.init.accept(this);
        forStmt.cond.accept(this);
        if (!forStmt.cond.getType().isLessThan(Type.PrimitiveType.booleanType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.booleanType(), forStmt.cond.getType());
        Scope.pushScope(forStmt);
        if (forStmt.update != null) forStmt.update.accept(this);
        forStmt.stmt.accept(this);
        Scope.popScope();
    }

    @Override
    public void visit(Stmt.BreakStmt breakStmt) {
        if (Scope.getInstance().getContext(Context.LOOP) == null)
            throw new BreakOutsideLoopException();;
    }

    @Override
    public void visit(Stmt.ContinueStmt continueStmt) {
        if (Scope.getInstance().getContext(Context.LOOP) == null)
            throw new ContinueOutsideLoopException();
    }

    @Override
    public void visit(Stmt.ReturnStmt returnStmt) {
        returnStmt.expr.accept(this);
        ContextualScoped context = Scope.getInstance().getContext(Context.FUNCTION);
        if (context == null)
            throw new ReturnOutsideFunctionException();
        Type exprType = returnStmt.expr.getType();
        Type returnType = ((Decl.FunctionDecl) context).returnType;
        if (!exprType.isLessThan(returnType))
            throw new IncompatibleTypesException(returnType, exprType);
    }

    @Override
    public void visit(Stmt.PrintStmt printStmt) {
        for (Expr expr : printStmt.exprs) {
            expr.accept(this);

        }
    }

    @Override
    public void visit(Stmt.BlockStmt blockStmt) {
        blockStmt.stmtBlock.accept(this);
    }

    @Override
    public void visit(StmtBlock stmtBlock) {
        Scope scope = Scope.pushScope(null);
        for (Decl.VariableDecl variableDecl : stmtBlock.variableDecls) {
            scope.setEntry(variableDecl.id, variableDecl);
        }
        for (Stmt stmt : stmtBlock.stmts) {
            stmt.accept(this);
        }
        Scope.popScope();
    }

    @Override
    public void visit(LValue.SimpleLVal lValue) {}

    @Override
    public void visit(Expr.ConstExpr constExpr) {
    }


    @Override
    public void visit(Expr.ThisExpr thisExpr) {
        ContextualScoped classScope = Scope.getInstance().getContext(Context.CLASS);
        if (classScope == null)
            throw new ThisOutsideClassException();
    }

    @Override
    public void visit(Expr.CallExpr callExpr) {
        callExpr.call.accept(this);
    }

    @Override
    public void visit(Expr.AssignExpr assignExpr) {
        assignExpr.leftHandSide.accept(this);
        assignExpr.rightHandSide.accept(this);
        Type leftType = assignExpr.leftHandSide.getType();
        Type rightType = assignExpr.rightHandSide.getType();
        System.out.println(leftType);
        System.out.println(rightType);
        if (!rightType.isLessThan(leftType))
            throw new IncompatibleTypesException(leftType, rightType);
    }

    @Override
    public void visit(Expr.LValExpr lValExpr) {
        lValExpr.lValue.accept(this);
    }

    @Override
    public void visit(Expr.BinOpExpr binOpExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr addExpr) {
        addExpr.expr1.accept(this);
        Expr.BinOpExpr.AddExpr castedExpr = addExpr.downcast();
        castedExpr.accept(this);
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.IntAddExpr intAddExpr) {
        typeCheckBinaryExpr(intAddExpr, List.of(Type.PrimitiveType.IntegerType.integerType()));
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.DoubleAddExpr doubleAddExpr) {
        typeCheckBinaryExpr(doubleAddExpr, List.of(Type.PrimitiveType.IntegerType.doubleType()));
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.StringAddExpr stringAddExpr) {
        typeCheckBinaryExpr(stringAddExpr, List.of(Type.PrimitiveType.IntegerType.stringType()));
    }

    @Override
    public void visit(Expr.BinOpExpr.AddExpr.ArrayAddExpr arrayAddExpr) {
        typeCheckBinaryExpr(arrayAddExpr, List.of(arrayAddExpr.expr1.getType()));
    }

    public void typeCheckBinaryExpr(Expr.BinOpExpr binOpExpr, List<Type> expectedTypes) {
        binOpExpr.expr1.accept(this);
        binOpExpr.expr2.accept(this);
        Type type1 = binOpExpr.expr1.getType();
        Type type2 = binOpExpr.expr2.getType();
        for (Type expectedType : expectedTypes) {
            if (type1.isLessThan(expectedType)) {
                if (type2.isLessThan(expectedType)) return;
                else throw new IncompatibleTypesException(expectedType, type2);
            }
        }
        throw new IncompatibleTypesException(expectedTypes, type1);
    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr arithExpr) {
        List<Type> expectedTypes = Arrays.asList(Type.PrimitiveType.integerType(), Type.PrimitiveType.doubleType());
        typeCheckBinaryExpr(arithExpr, expectedTypes);
    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.SubExpr subExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.MultExpr multExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.DivExpr divExpr) {

    }

    @Override
    public void visit(Expr.BinOpExpr.ArithExpr.ModExpr modExpr) {

    }


    @Override
    public void visit(Expr.BinOpExpr.CompExpr compExpr) {;
        List<Type> expectedTypes = Arrays.asList(Type.PrimitiveType.integerType(), Type.PrimitiveType.doubleType());
        typeCheckBinaryExpr(compExpr, expectedTypes);    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.LessExpr lessExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) lessExpr);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.LessEqExpr lessEqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) lessEqExpr);

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.GreaterExpr greaterExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) greaterExpr);

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.GreaterEqExpr greaterEqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) greaterEqExpr);

    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.EqExpr eqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) eqExpr);
    }

    @Override
    public void visit(Expr.BinOpExpr.CompExpr.NotEqExpr notEqExpr) {
        this.visit((Expr.BinOpExpr.CompExpr) notEqExpr);
    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr logicalExpr) {
        typeCheckBinaryExpr(logicalExpr, List.of(Type.PrimitiveType.booleanType()));
    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr.AndExpr andExpr) {
        this.visit((Expr.BinOpExpr.LogicalExpr) andExpr);
    }

    @Override
    public void visit(Expr.BinOpExpr.LogicalExpr.OrExpr orExpr) {
        this.visit((Expr.BinOpExpr.LogicalExpr) orExpr);
    }

    @Override
    public void visit(Expr.UnOpExpr.ArithExpr.MinusExpr minusExpr) {
        minusExpr.expr.accept(this);
        if (!minusExpr.expr.getType().isLessThan(Type.PrimitiveType.integerType()) &&
                !minusExpr.expr.getType().isLessThan(Type.PrimitiveType.doubleType()))
            throw new IncompatibleTypesException(Arrays.asList(Type.PrimitiveType.integerType(),
                    Type.PrimitiveType.doubleType()), minusExpr.expr.getType());
    }

    @Override
    public void visit(Expr.UnOpExpr.LogicalExpr.NotExpr notExpr) {
        notExpr.expr.accept(this);
        if (!notExpr.expr.getType().isLessThan(Type.PrimitiveType.booleanType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.booleanType(), notExpr.expr.getType());
    }


    @Override
    public void visit(Call.SimpleCall simpleCall) {
        Decl decl = Scope.getInstance().getEntry(simpleCall.id);
        if (!(decl instanceof Decl.FunctionDecl))
            throw new NotCallableIdentifierException(simpleCall.id);
        this.checkCallArguments(simpleCall, (Decl.FunctionDecl) decl);
    }

    @Override
    public void visit(Call.DottedCall dottedCall) {
        dottedCall.expr.accept(this);
        Type exprType = dottedCall.expr.getType();
        if (!(exprType instanceof Type.NonPrimitiveType))
            throw new MethodNotFoundException(dottedCall.id);
        Decl.ClassDecl classDecl = (Decl.ClassDecl) Scope.getInstance().getEntry(((Type.NonPrimitiveType) exprType).id);
        Decl.FunctionDecl method = classDecl.getMethod(dottedCall.id);
        if (method == null)
            throw new MethodNotFoundException(dottedCall.id, exprType);
        this.checkCallArguments(dottedCall, method);
    }

    @Override
    public void visit(Constant.IntConst intConst) {

    }

    @Override
    public void visit(Constant.BoolConst boolConst) {

    }

    public void checkCallArguments(Call call, Decl.FunctionDecl functionDecl) {
        for (Expr actual : call.actuals) {
            actual.accept(this);
        }
        if (call.actuals.size() != functionDecl.formals.size())
            throw new ArgumentNumberMismatchException(functionDecl.id, functionDecl.formals.size(), call.actuals.size());
        for (int i = 0; i < call.actuals.size(); i++) {
            Type formalType = functionDecl.formals.get(i).type;
            Type actualType = call.actuals.get(i).getType();
            if (!actualType.isLessThan(formalType))
                throw new IncompatibleTypesException(formalType, actualType);
        }
    }

    @Override
    public void visit(DoubleConst doubleConst) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(StringConst stringConst) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ReadIntExpr readIntExpr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ReadLineExpr readLineExpr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(ItodExpr itodExpr) {
        itodExpr.expr.accept(this);
        if (!itodExpr.expr.getType().isLessThan(Type.PrimitiveType.integerType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.integerType(), itodExpr.expr.getType());
    }

    @Override
    public void visit(DtoiExpr dtoiExpr) {
        dtoiExpr.expr.accept(this);
        if (!dtoiExpr.expr.getType().isLessThan(Type.PrimitiveType.doubleType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.doubleType(), dtoiExpr.expr.getType());
    }

    @Override
    public void visit(ItobExpr itobExpr) {
        itobExpr.expr.accept(this);
        if (!itobExpr.expr.getType().isLessThan(Type.PrimitiveType.integerType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.integerType(), itobExpr.expr.getType());
    }

    @Override
    public void visit(BtoiExpr btoiExpr) {
        btoiExpr.expr.accept(this);
        if (!btoiExpr.expr.getType().isLessThan(Type.PrimitiveType.booleanType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.booleanType(), btoiExpr.expr.getType());
    }

    @Override
    public void visit(ArrInit arrInit) {
        if (!(arrInit.expr.getType().isLessThan(Type.PrimitiveType.integerType())))
            throw new IncompatibleTypesException(Type.PrimitiveType.integerType() ,arrInit.expr.getType());
        if (arrInit.type.isLessThan(Type.voidType()))
            throw new IncompatibleTypesException(Type.voidType(), Type.voidType());
    }

    @Override
    public void visit(IndexedLVal lValue) {

        if (!(lValue.expr.getType() instanceof Type.ArrayType))
            throw new IncompatibleTypesException(Type.arrayType(null), lValue.expr.getType());

        if (!lValue.index.getType().isLessThan(Type.PrimitiveType.integerType()))
            throw new IncompatibleTypesException(Type.PrimitiveType.integerType() ,lValue.index.getType());
        
    }

    
    
}

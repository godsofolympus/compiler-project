package compiler.AST;

public abstract class Stmt {

    public static ExprStmt exprStmt() {return new ExprStmt();}
    public static class ExprStmt extends Stmt {}

    public static IfStmt ifStmt() {return new IfStmt();}
    public static class IfStmt extends Stmt {}

    public static WhileStmt whileStmt() {return new WhileStmt();}
    public static class WhileStmt extends Stmt {}

    public static ForStmt forStmt() {return new ForStmt();}
    public static class ForStmt extends Stmt {}

    public static BreakStmt breakStmt() {return new BreakStmt();}
    public static class BreakStmt extends Stmt {}

    public static ContinueStmt continueStmt() {return new ContinueStmt();}
    public static class ContinueStmt extends Stmt {}

    public static ReturnStmt returnStmt() {return new ReturnStmt();}
    public static class ReturnStmt extends Stmt {}

    public static PrintStmt printStmt() {return new PrintStmt();}
    public static class PrintStmt extends Stmt {}

    public static BlockStmt blockStmt() {return new BlockStmt();}
    public static class BlockStmt extends Stmt {}
}

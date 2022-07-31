package compiler.AST;

import java.util.List;

public abstract class Stmt {

    public static ExprStmt exprStmt(Expr expr) {return new ExprStmt(expr);}
    public static class ExprStmt extends Stmt {
        public Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }
    }

    public static IfStmt ifStmt(Expr cond, Stmt stmt, Stmt elseStmt) {return new IfStmt(cond, stmt, elseStmt);}
    public static class IfStmt extends Stmt {
        public Expr cond;
        public Stmt stmt;
        public Stmt elseStmt;

        public IfStmt(Expr cond, Stmt stmt, Stmt elseStmt) {
            this.cond = cond;
            this.stmt = stmt;
            this.elseStmt = elseStmt;
        }
    }

    public static WhileStmt whileStmt(Expr cond, Stmt stmt) {return new WhileStmt(cond, stmt);}
    public static class WhileStmt extends Stmt {
        public Expr cond;
        public Stmt stmt;

        public WhileStmt(Expr cond, Stmt stmt) {
            this.cond = cond;
            this.stmt = stmt;
        }
    }

    public static ForStmt forStmt(Expr init, Expr cond, Expr update, Stmt stmt) {return new ForStmt(init, cond, update, stmt);}
    public static class ForStmt extends Stmt {
        public Expr init;
        public Expr cond;
        public Expr update;
        public Stmt stmt;

        public ForStmt(Expr init, Expr cond, Expr update, Stmt stmt) {
            this.init = init;
            this.cond = cond;
            this.update = update;
            this.stmt = stmt;
        }
    }

    public static BreakStmt breakStmt() {return new BreakStmt();}
    public static class BreakStmt extends Stmt {}

    public static ContinueStmt continueStmt() {return new ContinueStmt();}
    public static class ContinueStmt extends Stmt {}

    public static ReturnStmt returnStmt(Expr expr) {return new ReturnStmt(expr);}
    public static class ReturnStmt extends Stmt {
        public Expr expr;

        public ReturnStmt(Expr expr) {
            this.expr = expr;
        }
    }

    public static PrintStmt printStmt(List<Expr> exprs) {return new PrintStmt(exprs);}
    public static class PrintStmt extends Stmt {
        public List<Expr> exprs;

        public PrintStmt(List<Expr> exprs) {
            this.exprs = exprs;
        }
    }

    public static BlockStmt blockStmt(StmtBlock stmtBlock) {return new BlockStmt(stmtBlock);}
    public static class BlockStmt extends Stmt {
        public StmtBlock stmtBlock;

        public BlockStmt(StmtBlock stmtBlock) {
            this.stmtBlock = stmtBlock;
        }
    }
}

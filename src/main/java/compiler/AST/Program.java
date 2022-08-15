package compiler.AST;

import java.util.ArrayList;
import java.util.List;

public class Program {
    public List<Decl> decls;
    private final List<Decl.VariableDecl> globalVars;
    private final List<Decl.FunctionDecl> functionDecls;

    private final List<Decl.ClassDecl> classDecls;

    public Program(List<Decl> decls) {
        this.decls = decls;
        this.globalVars = new ArrayList<>();
        this.functionDecls = new ArrayList<>();
        this.classDecls = new ArrayList<>();
        this.setDecls();
    }

    public List<Decl.VariableDecl> getGlobalVars() {
        return globalVars;
    }

    public List<Decl.FunctionDecl> getFunctionDecls() {
        return functionDecls;
    }

    public List<Decl.ClassDecl> getClassDecls() {
        return classDecls;
    }

    private void setDecls() {
        for (Decl decl : decls) {
            if (decl instanceof Decl.VariableDecl) {
                Decl.VariableDecl globalVar = (Decl.VariableDecl) decl;
                globalVar.isGlobal = true;
                this.globalVars.add(globalVar);
            } else if (decl instanceof Decl.FunctionDecl) {
                functionDecls.add((Decl.FunctionDecl) decl);
                if (decl.id.equals("main"))
                    ((Decl.FunctionDecl) decl).isMain = true;
            }
            else classDecls.add((Decl.ClassDecl) decl);
        }
    }
}

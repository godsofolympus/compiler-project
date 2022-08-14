package compiler.AST;

import java.util.ArrayList;
import java.util.List;

public class Program {
    public List<Decl> decls;
    private final List<Decl.VariableDecl> globalVars;
    private final List<Decl.FunctionDecl> functionDecls;

    public Program(List<Decl> decls) {
        this.decls = decls;
        this.globalVars = new ArrayList<>();
        this.functionDecls = new ArrayList<>();
        this.setGlobalVars();
        this.setFunctionDecls();
    }

    public List<Decl.VariableDecl> getGlobalVars() {
        return globalVars;
    }

    private void setGlobalVars() {
        for (Decl decl : decls) {
            if (decl instanceof Decl.VariableDecl) {
                Decl.VariableDecl globalVar = (Decl.VariableDecl) decl;
                globalVar.isGlobal = true;
                this.globalVars.add(globalVar);
            }
        }
    }

    public List<Decl.FunctionDecl> getFunctionDecls() {
        return functionDecls;
    }

    public void setFunctionDecls() {
        for (Decl decl : decls) {
            if (decl instanceof Decl.FunctionDecl)
                functionDecls.add((Decl.FunctionDecl) decl);
        }
    }
}

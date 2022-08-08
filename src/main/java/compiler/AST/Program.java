package compiler.AST;

import java.util.ArrayList;
import java.util.List;

public class Program {
    public List<Decl> decls;
    public Program(List<Decl> decls) {
        this.decls = decls;
    }

    public List<Variable> getGlobalVars() {
        List<Variable> globalVars = new ArrayList<>();
        for (Decl decl : decls) {
            if (decl instanceof Decl.VariableDecl) globalVars.add(((Decl.VariableDecl) decl).variable);
        }
        return globalVars;
    }

    public List<Decl.FunctionDecl> getFunctionDecls() {
        List<Decl.FunctionDecl> functionDecls = new ArrayList<>();
        for (Decl decl : decls) {
            if (decl instanceof Decl.FunctionDecl)
                functionDecls.add((Decl.FunctionDecl) decl);
        }
        return functionDecls;
    }
}

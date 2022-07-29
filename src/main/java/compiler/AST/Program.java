package compiler.AST;

import java.util.List;

public class Program {
    public List<Decl> decls;
    public Program(List<Decl> decls) {
        this.decls = decls;
    }
}

package compiler.models;

import compiler.AST.Decl;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    public Scope parent;
    public Map<String, Decl> symbolTable;

    public Scope(Scope parent) {
        this.parent = parent;
        this.symbolTable = new HashMap<>();
    }
}

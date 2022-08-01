package compiler.models;

import compiler.AST.Decl;
import compiler.Exceptions.SymbolNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private static Scope instance;
    public static Scope getInstance() {
        if (instance == null) instance = new Scope(null);
        return instance;
    }
    public Scope parent;
    private final Map<String, Decl> symbolTable;

    private Scope(Scope parent) {
        this.parent = parent;
        this.symbolTable = new HashMap<>();
    }

    public Scope pushScope() {
        instance = new Scope(instance);
        return instance;
    }

    public Scope popScope() {
        instance = instance.parent;
        return instance;
    }

    public Decl getEntry(String id) {
        Scope currentScope = instance;
        while (currentScope != null) {
            Decl entry = currentScope.symbolTable.get(id);
            if (entry != null) return entry;
            currentScope = currentScope.parent;
        }
        throw new SymbolNotFoundException(id);
    }

    public void setEntry(String id, Decl decl) {
        instance.symbolTable.put(id, decl);
    }
}

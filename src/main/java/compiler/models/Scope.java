package compiler.models;

import compiler.AST.Decl;
import compiler.Exceptions.SymbolNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private static Scope instance;
    public static Scope getInstance() {
        if (instance == null) instance = new Scope(null, null);
        return instance;
    }
    public Scope parent;
    public ContextualScoped context;

    private final Map<String, Decl> symbolTable;

    private Scope(Scope parent, ContextualScoped context) {
        this.parent = parent;
        this.context = context;
        this.symbolTable = new HashMap<>();
    }

    public static Scope pushScope(ContextualScoped context) {
        instance = new Scope(getInstance(), context);
        return instance;
    }

    public static Scope popScope() {
        instance = getInstance().parent;
        return instance;
    }

    public Decl getEntry(String id) {
        Scope currentScope = this;
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

    public ContextualScoped getContext(Context context) {
        Scope currentScope = this;
        while (currentScope != null) {
            if (currentScope.context != null && currentScope.context.getContext() == context) return currentScope.context;
            currentScope = currentScope.parent;
        }
        return null;
    }

}

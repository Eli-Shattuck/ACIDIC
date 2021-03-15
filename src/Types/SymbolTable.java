package Types;

import Types.Exceptions.SymbolNotFound;
import Types.Values.Value;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Value> varTable;
    private SymbolTable parent;

    public SymbolTable(SymbolTable parent) {
        this.varTable = new HashMap<>();
        this.parent = parent;
    }

    public SymbolTable() {
        this(null);
    }

    public Value get(String name) {
        if(varTable.containsKey(name)) {
            return varTable.get(name);
        } else if(parent.varTable.containsKey(name)) {
            return parent.get(name);
        } else {
            return null;
        }
    }

    public void put(String name, Value value) {
        varTable.put(name, value);
    }

    public void remove(String name) {
        varTable.remove(name);
    }
}

package Interpreting;

import Types.ExpTreeNode;
import Types.Result;
import Types.SymbolTable;
import Types.Values.Value;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    private static SymbolTable varTable = new SymbolTable();

    public static Result<Value> Interpret(ExpTreeNode root) {
        //System.out.println(root.toString());
        Result<Value> result = root.getInterpreterType().getOnVisit().apply(root);
        return result;
    }

    public static SymbolTable getVarTable() {
        return varTable;
    }
}

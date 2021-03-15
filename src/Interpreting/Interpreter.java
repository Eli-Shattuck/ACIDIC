package Interpreting;

import Types.ExpTreeNode;
import Types.Result;
import Types.Values.Value;

public class Interpreter {

    public static Result<Value> Interpret(ExpTreeNode root) {
        //System.out.println(root.toString());
        Result<Value> result = root.getInterpreterType().getOnVisit().apply(root);
        return result;
    }
}

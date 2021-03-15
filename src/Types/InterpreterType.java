package Types;

import Interpreting.Interpreter;
import Types.Exceptions.SymbolNotFound;
import Types.Values.Number;
import Types.Values.String;
import Types.Values.Value;

import java.util.function.Function;

public enum InterpreterType {
    NUMBER_NODE(        "<NUMBER NODE>",         (node)->{
        if(node.getLexToken().getLexType() == LexType.INT) {
            return new Result<Value>().success(
                    new Number(Long.parseLong(node.getLexToken().getValue()), Number.INT)
            );
        }
        if(node.getLexToken().getLexType() == LexType.FLOAT) {
            return new Result<Value>().success(
                    new Number(Double.parseDouble(node.getLexToken().getValue()), Number.FLOAT)
            );
        }
        return null;
    }),
    BIN_OPERATOR_NODE(  "<BIN OPERATOR NODE>",   (node)->{
        ExpTreeNode left = node.getChildren().get(0);
        ExpTreeNode right = node.getChildren().get(1);

        Result<Value> ret = new Result<>();

        Value a = ret.register(left.getInterpreterType().getOnVisit().apply(left));
        if(ret.error()) return ret;
        Value b = ret.register(right.getInterpreterType().getOnVisit().apply(right));
        if(ret.error()) return ret;

        Value result;

        if(node.getLexToken().getLexType() == LexType.ADD) {
            return Number.add(a,b);
        }
        if(node.getLexToken().getLexType() == LexType.SUB) {
            return Number.sub(a,b);
        }
        if(node.getLexToken().getLexType() == LexType.MUL) {
            return Number.mul(a,b);
        }
        if(node.getLexToken().getLexType() == LexType.DIV) {
            return Number.div(a,b);
        }
        if(node.getLexToken().getLexType() == LexType.POW) {
            return Number.pow(a,b);
        }
        return null;
    }),
    UNARY_OPERATOR_NODE("<UNARY OPERATOR NODE>", (node)->{
        ExpTreeNode next = node.getChildren().get(0);
        next.getInterpreterType().getOnVisit().apply(next);

        Result<Value> ret = new Result<>();

        Value a = ret.register(next.getInterpreterType().getOnVisit().apply(next));
        if(ret.error()) return ret;

        if(node.getLexToken().getLexType() == LexType.ADD) return ret.success(a);
        if(node.getLexToken().getLexType() == LexType.SUB) return Number.invert(a);

        return null;
    }),
    SYMBOL_NODE("<SYMBOL NODE>", (node)->{
        return new Result<Value>().success(new String(node.getLexToken().getValue()));
    }),
    VAR_ACCESS_NODE("<VAR ACCESS NODE>", (node)->{
        Result<Value> res = new Result<>();
        Value var = Interpreter.getVarTable().get(node.getLexToken().getValue());
        if(var == null) return res.failure(new SymbolNotFound("SYMBOL NOT FOUND"));
        return res.success(var);
    }),
    VAR_ASSIGNMENT_NODE("<VAR ASSIGNMENT NODE>", (node)->{
        ExpTreeNode var = node.getChildren().get(0);
        ExpTreeNode val = node.getChildren().get(1);

        Result<Value> ret = new Result<>();

        Value a = ret.register(var.getInterpreterType().getOnVisit().apply(var));
        if(ret.error()) return ret;
        Value b = ret.register(val.getInterpreterType().getOnVisit().apply(val));
        if(ret.error()) return ret;

        if(a instanceof String){
            Interpreter.getVarTable().put(((String) a).getVal(), b);
        }

        return ret.success(b);
    });
    private java.lang.String name;
    private Function<ExpTreeNode, Result<Value>> onVisit;

    InterpreterType(java.lang.String name, Function<ExpTreeNode, Result<Value>> onVisit) {
        this.name = name;
        this.onVisit = onVisit;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public Function<ExpTreeNode, Result<Value>> getOnVisit() {
        return onVisit;
    }
}

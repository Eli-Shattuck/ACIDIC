package Types;

import Types.Values.Number;
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
    });

    private String name;
    private Function<ExpTreeNode, Result<Value>> onVisit;

    InterpreterType(String name, Function<ExpTreeNode, Result<Value>> onVisit) {
        this.name = name;
        this.onVisit = onVisit;
    }

    @Override
    public String toString() {
        return name;
    }

    public Function<ExpTreeNode, Result<Value>> getOnVisit() {
        return onVisit;
    }
}

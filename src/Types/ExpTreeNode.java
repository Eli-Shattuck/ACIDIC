package Types;

import java.util.Arrays;
import java.util.List;

public class ExpTreeNode {
    private LexToken lexToken;
    private InterpreterType interpreterType;
    private List<ExpTreeNode> children;

    public ExpTreeNode(LexToken lexToken, InterpreterType interpreterType, List<ExpTreeNode> children) {
        this.lexToken = lexToken;
        this.interpreterType = interpreterType;
        this.children = children;
    }

    public ExpTreeNode(LexToken lexToken, InterpreterType interpreterType, ExpTreeNode... children) {
        this(lexToken, interpreterType, Arrays.asList(children));
    }

    public LexToken getLexToken() {
        return lexToken;
    }

    public List<ExpTreeNode> getChildren() {
        return children;
    }

    public InterpreterType getInterpreterType() {
        return interpreterType;
    }

    @Override
    public String toString() {
        return toString("");
    }

    private String toString(String indent) {
        String next = indent + "\t";
        return indent + "ExpTreeNode{" +
                '\n' + next + "token=" + lexToken +
                '\n' + next + "parseType=" + interpreterType +
                '\n' + next + "children=" + getChildrenAsString(next) +
                '\n' + indent + '}';
    }

    private String getChildrenAsString(String indent) {
        //System.out.printf("child={%b}\n", getChildren()==null);
        if(getChildren() == null || getChildren().size() < 1) return "[]";
        StringBuilder ret = new StringBuilder("[\n");
        for (int i = 0; i < getChildren().size(); i++) {
            ExpTreeNode child = getChildren().get(i);
            if(child == null) ret.append("null");
            else ret.append(child.toString(indent));
            if(i < getChildren().size()-1) ret.append(",\n");
        }
        ret.append("\n").append(indent).append("]");
        return ret.toString();
    }
}

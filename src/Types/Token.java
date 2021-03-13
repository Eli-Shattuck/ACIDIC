package Types;

/*
 * class to hold a token represented as a Type, and a string holding a Value
 */
public class Token {
    private Type type;
    private String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Token{%s : \"%s\"}", type.toString(), value);
    }
}

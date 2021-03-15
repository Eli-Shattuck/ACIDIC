package Types;

/*
 * class to hold a token represented as a Type, and a string holding a Value
 */
public class LexToken {
    private LexType lexType;
    private String value;

    public LexToken(LexType lexType, String value) {
        this.lexType = lexType;
        this.value = value;
    }

    public LexType getLexType() {
        return lexType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Token{%s : \"%s\"}", lexType.toString(), value == null ? "" : value);
    }
}

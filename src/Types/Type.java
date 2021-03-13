package Types;

import java.util.Arrays;
import java.util.List;

public enum Type {
    NUMBER(null, "[.0-9]", "[.0-9]"),
    INT("<int>",null,null),
    FLOAT("<float>",null,null),
    STRING("<string>", "[\"]", "[\"]"),
    SYMBOL("<symbol>","[_A-Z]","[_A-Z0-9]"),
    ADD("<+>","[\\+]",null),
    SUB("<->","[\\-]",null),
    L_PAREN("<(>", "[\\(]", null),
    R_PAREN("<)>", "[\\)]", null),
    R_CURLY_BRACKET("<{>", "[\\{]", null),
    L_CURLY_BRACKET("<}>", "[\\}]", null),
    COMMA("<,>", "[\\,]", null);

    private String rep;
    public final String FIRST_REGEX;
    public final String REST_REGEX;

    Type(String rep, String fregex, String rregex) {
        this.rep = rep;
        this.FIRST_REGEX = fregex;
        this.REST_REGEX = rregex;
    }

    public static List<Type> getAllPunctuation() {
        return Arrays.asList(L_PAREN,R_PAREN,R_CURLY_BRACKET,L_CURLY_BRACKET,COMMA);
    }
    public static List<Type> getAllOperators() {
        return Arrays.asList(ADD, SUB);
    }

    @Override
    public String toString() {
        return this.rep;
    }
}

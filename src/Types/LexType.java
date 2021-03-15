package Types;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public enum LexType {
    NUMBER(         null,       "[.0-9]",   "[.0-9]"),
    INT(            "<INT>",    null,       null),
    FLOAT(          "<FLOAT>",  null,       null),
    STRING(         "<STRING>", "[\"]",     "[\"]"),
    SYMBOL(         "<SYMBOL>", "[_A-Z]",   "[_A-Z0-9]"),
    KEYWORD(        "<KEYWORD>","[_A-Z]",   "[_A-Z0-9]"),
    POW(            "<**>",     "[\\*]",    null),
    ADD(            "<+>",      "[\\+]",    null),
    SUB(            "<->",      "[\\-]",    null),
    MUL(            "<*>",      "[\\*]",    null),
    DIV(            "</>",      "[\\/]",    null),
    ASSIGNMENT(     "<=>",      "[\\=]",    null),
    L_PAREN(        "<(>",      "[\\(]",    null),
    R_PAREN(        "<)>",      "[\\)]",    null),
    R_CURLY_BRACKET("<{>",      "[\\{]",    null),
    L_CURLY_BRACKET("<}>",      "[\\}]",    null),
    COMMA(          "<,>",      "[\\,]",    null),
    SEMICOLON(      "<;>",      "[\\;]",    null);

    private String rep;
    public final String FIRST_REGEX;
    public final String REST_REGEX;

    LexType(String rep, String fregex, String rregex) {
        this.rep = rep;
        this.FIRST_REGEX = fregex;
        this.REST_REGEX = rregex;
    }

    public static List<LexType> getAllPunctuationList() {
        return Arrays.asList(L_PAREN,R_PAREN,R_CURLY_BRACKET,L_CURLY_BRACKET,COMMA,SEMICOLON);
    }
    public static HashSet<LexType> getAllPunctuationSet() {
        return new HashSet<>(getAllPunctuationList());
    }
    public static List<LexType> getAllOperatorsList() {
        return Arrays.asList(POW, ADD, SUB, MUL, DIV, ASSIGNMENT);
    }
    public static HashSet<LexType> getAllOperatorsSet() {
        return new HashSet<>(getAllOperatorsList());
    }
    @Override
    public String toString() {
        return this.rep;
    }
}

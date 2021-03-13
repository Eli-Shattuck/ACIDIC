package Types;

public enum Type {
    NUMBER("<number>","[.0-9]","[.0-9]"),
    STRING("<string>", "[\"]", "[\"]"),
    SYMBOL("<symbol>","[_A-Z]","[_A-Z0-9]"),
    OPERATION("<operation>","[\\+\\-\\/\\*]","[\\+\\-\\/\\*]"),
    PUNCTUATION("<punctuation>", "[\\(\\)\\{\\},;]", "[\\(\\)\\{\\},;]");

    private String rep;
    public final String FIRST_REGEX;
    public final String REST_REGEX;

    Type(String rep, String fregex, String rregex) {
        this.rep = rep;
        this.FIRST_REGEX = fregex;
        this.REST_REGEX = rregex;
    }

    @Override
    public String toString() {
        return this.rep;
    }
}

public enum Type {
    NUMBER("<number>"),
    STRING("<string>"),
    SYMBOL("<symbol>"),
    ADD("<+>"),
    SEMICOLON("<;>"),
    L_PAREN("<(>"),
    R_PAREN("<)>"),
    ASIGNMENT("<=>");

    private String rep;

    Type(String rep) {
        this.rep = rep;
    }

    @Override
    public String toString() {
        return this.rep;
    }
}

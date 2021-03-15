package Types;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Keywords {
    DEF("DEF");

    public static Set<String> KEYWORD_SET = new HashSet<>(Arrays.asList("DEF"));

    private String name;

    Keywords(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

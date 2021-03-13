import Lexing.Lexer;
import Types.Token;
import Types.EndOfStringException;

import java.util.Iterator;

public class Main {
    /*
     * Example ACIDIC programs:
     *
     * PRINT ("HELLO, WORLD!");
     *
     * FOO = (3.1416 + 3.) + .12;
     * PRINT (FOO);
     *
     * FUNCTION FOO (BAR, BAZ) {
     *  RETURN BAR*BAZ
     * }
     */

    public static void main(String[] args) throws EndOfStringException {
        Lexer lexer = new Lexer();
        //String program = "PRINT (\"HELLO, WORLD!\");";
        //String program = "FOO = (3.1415 + 3.) + .12;";
        //String program = "FOO = \"BAR\";";
        //String program = "foo = \"bar\";"; //this program should fail
        String program =
                "FUNCTION FOO (BAR, BAZ) {\n" +
                        "\tRETURN BAR*BAZ\n" +
                        "}";
        Iterator<Token> tokens = lexer.lex(program);
        while (tokens.hasNext())
            System.out.println(tokens.next());
    }
}

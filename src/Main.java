import Interpreting.Interpreter;
import Lexing.Lexer;
import Parsing.Parser;
import Types.*;
import Types.Values.Value;

import java.util.List;
import java.util.Scanner;

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

    public static void main(String[] args) {
        //String program = "PRINT (\"HELLO, WORLD!\");";
        //String program = "FOO = (3.1415 + 3.) + .12 - (1+2);";
        //String program = "FOO = \"BAR\";";
        //String program = "foo = \"bar\";"; //this program should fail
//        String program =
//                "FUNCTION FOO (BAR, BAZ) {\n" +
//                        "\tRETURN BAR-BAZ;\n" +
//                        "}";
        //String program = "1 + (2 + 3";
        Scanner in = new Scanner(System.in);
        while(true) {
            Result<List<LexToken>> lexTokens = Lexer.lex(in.nextLine());
            if (lexTokens.error()) {
                System.err.println(lexTokens.getError());
                continue;
            }
//            for (LexToken lexToken : lexTokens)
//                System.out.println(lexToken);

            Result<ExpTreeNode> parsedTokens = Parser.Parse(lexTokens.getVal());
            if (parsedTokens.error()) {
                System.err.println(parsedTokens.getError());
                continue;
            }
//            System.out.println(parsedTokens.getTree());

            Result<Value> result = Interpreter.Interpret(parsedTokens.getVal());
            if (result.error()) {
                System.err.println(result.getError());
                continue;
            }
            System.out.println(result.getVal());
        }
        //System.out.println(new ExpTreeNode(new Token(Type.ASSIGNMENT, null), new ExpTreeNode(new Token(Type.SYMBOL, "FOO")), new ExpTreeNode(new Token(Type.STRING, "BAR"))));
    }
}

import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws EndOfStringException {
        Lexer lexer = new Lexer();
        //Iterator<Token> tokens = lexer.lex("PRINT (\"HELLO, WORLD!\");");
        //Iterator<Token> tokens = lexer.lex("FOO = (3.1415 + 3.) + .12;");
        //Iterator<Token> tokens = lexer.lex("FOO = \"BAR\";");
        Iterator<Token> tokens = lexer.lex("foo = \"bar\";");
        while (tokens.hasNext())
            System.out.println(tokens.next());
    }
}

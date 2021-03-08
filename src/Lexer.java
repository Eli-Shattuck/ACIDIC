import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * class to lex an ACIDIC program into tokens, defined in Token.java
 */

public class Lexer {
    public Lexer() {

    }

    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a number, and returns it as a String
     */
    private String scanNumber(char first, CharacterIterator chars) {
        StringBuilder ret = new StringBuilder(String.format("%c", first));
        while (chars.current() != CharacterIterator.DONE && String.format("%c", chars.current()).matches("[.0-9]")) {
            ret.append(chars.current());
            chars.next();
        }
        return ret.toString();
    }
    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a variable name, and returns it as a String
     */
    private String scanSymbol(char first, CharacterIterator chars) {
        StringBuilder ret = new StringBuilder(String.format("%c", first));
        while (chars.current() != CharacterIterator.DONE && String.format("%c", chars.current()).matches("[_A-Z0-9]")) {
            ret.append(chars.current());
            chars.next();
        }
        return ret.toString();
    }

    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a string, and returns it as a String
     */
    private String scanString(char first, CharacterIterator chars) throws EndOfStringException {
        StringBuilder ret = new StringBuilder();
        while (chars.current() != '"') {
            if (chars.current() == CharacterIterator.DONE)
                throw new EndOfStringException("NO ' \" ' AT END OF STRING LITERAL");
            ret.append(chars.current());
            chars.next();
        }
        chars.next();
        return ret.toString();
    }

    /*
     * takes in a program as a string and looks through the string one at a time,
     * lexing it into an iterator of Tokens
     */
    public Iterator<Token> lex(String programString) throws EndOfStringException {
       CharacterIterator program = new StringCharacterIterator(programString);
        List<Token> tokens = new ArrayList<>();

        while (program.current() != CharacterIterator.DONE) {
            char c = program.current();
            program.next();

            if ("\t\n ".indexOf(c) != -1) continue; //disregard any whitespace

            if (c == '+') tokens.add(new Token(Type.ADD, "")); //add addition token if c is +
            if (c == ';') tokens.add(new Token(Type.SEMICOLON, "")); //add semicolon token if c is ;
            if (c == '(') tokens.add(new Token(Type.L_PAREN, "")); //add left paren token if c is (
            if (c == ')') tokens.add(new Token(Type.R_PAREN, "")); //add right paren token if c is )
            if (c == '=') tokens.add(new Token(Type.ASIGNMENT, "")); //add assignment token if c is =

            if (c == '"') tokens.add(new Token(Type.STRING, scanString(c, program))); //add string token if c is "

            if (String.format("%c", c).matches("[.0-9]")) tokens.add(new Token(Type.NUMBER, scanNumber(c, program))); //add number token if c matches a digit or a .
            if (String.format("%c", c).matches("[_A-Z]")) tokens.add(new Token(Type.SYMBOL, scanSymbol(c, program))); //add symbol token if c matches a letter or an _

            throw new IllegalArgumentException(String.format("UNKNOWN CHAR '%c'", c));
        }
        return tokens.iterator();
    }
}

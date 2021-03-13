package Lexing;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import Types.*;
import static utils.Utils.charToString;
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
        while (chars.current() != CharacterIterator.DONE && charToString(chars.current()).matches(Type.NUMBER.REST_REGEX)) {
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
        while (chars.current() != CharacterIterator.DONE && charToString(chars.current()).matches(Type.SYMBOL.REST_REGEX)) {
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
        while (!charToString(chars.current()).matches(Type.STRING.REST_REGEX)) {
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

            if (charToString(c).matches(Type.OPERATION.FIRST_REGEX)) {
                tokens.add(new Token(Type.OPERATION, charToString(c))); //add operation token if c is an operator
            }
            else if (charToString(c).matches(Type.PUNCTUATION.FIRST_REGEX)) {
                tokens.add(new Token(Type.PUNCTUATION, charToString(c))); //add punctuation token if c is punctuation
            }
            else if (charToString(c).matches(Type.STRING.FIRST_REGEX)) {
                tokens.add(new Token(Type.STRING, scanString(c, program))); //add string token if c is "
            }
            else if (charToString(c).matches(Type.NUMBER.FIRST_REGEX)) {
                tokens.add(new Token(Type.NUMBER, scanNumber(c, program))); //add number token if c matches a number
            }
            else if (charToString(c).matches(Type.SYMBOL.FIRST_REGEX)) {
                tokens.add(new Token(Type.SYMBOL, scanSymbol(c, program))); //add symbol token if c matches a symbol
            }
            else {
                throw new IllegalArgumentException(String.format("UNKNOWN CHAR '%c'", c));
            }
        }
        return tokens.iterator();
    }
}

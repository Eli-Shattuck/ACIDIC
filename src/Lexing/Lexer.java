package Lexing;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.IllegalFormatException;
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
     * parses the iterator for a number, and returns it as a Token
     */
    private Token scanNumber(char first, CharacterIterator chars) throws IllegalCharException {
        StringBuilder ret = new StringBuilder(String.format("%c", first));
        int dotCount = first == '.' ? 1 : 0;
        while (chars.current() != CharacterIterator.DONE && charToString(chars.current()).matches(Type.NUMBER.REST_REGEX)) {
            if(chars.current() == '.') dotCount++;
            ret.append(chars.current());
            chars.next();
        }
        if(dotCount == 0) {
            return new Token(Type.INT, ret.toString());
        } else if (dotCount == 1) {
            return new Token(Type.FLOAT, ret.toString());
        } else {
            throw new IllegalCharException("TOO MANY '.' IN NUMBER");
        }
    }
    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a variable name, and returns it as a Token
     */
    private Token scanSymbol(char first, CharacterIterator chars) {
        StringBuilder ret = new StringBuilder(String.format("%c", first));
        while (chars.current() != CharacterIterator.DONE && charToString(chars.current()).matches(Type.SYMBOL.REST_REGEX)) {
            ret.append(chars.current());
            chars.next();
        }
        return new Token(Type.SYMBOL, ret.toString());
    }

    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a string, and returns it as a Token
     */
    private Token scanString(char first, CharacterIterator chars) throws EndOfStringException {
        StringBuilder ret = new StringBuilder();
        while (!charToString(chars.current()).matches(Type.STRING.REST_REGEX)) {
            if (chars.current() == CharacterIterator.DONE)
                throw new EndOfStringException("NO ' \" ' AT END OF STRING LITERAL");
            ret.append(chars.current());
            chars.next();
        }
        chars.next();
        return new Token(Type.STRING, ret.toString());
    }

    /*
     * takes in a program as a string and looks through the string one at a time,
     * lexing it into an iterator of Tokens
     */
    public Iterator<Token> lex(String programString) throws EndOfStringException, IllegalCharException {
       CharacterIterator program = new StringCharacterIterator(programString);
        List<Token> tokens = new ArrayList<>();

        while (program.current() != CharacterIterator.DONE) {
            char c = program.current();
            program.next();

            int currTokens = tokens.size();

            if ("\t\n ".indexOf(c) != -1) continue; //disregard any whitespace
            for(Type op: Type.getAllOperators()) {
                if (charToString(c).matches(op.FIRST_REGEX)) {
                    tokens.add(new Token(op, null)); //add operation token if c is an operator
                }
            }
            for(Type punc: Type.getAllPunctuation()) {
                if (charToString(c).matches(punc.FIRST_REGEX)) {
                    tokens.add(new Token(punc, null)); //add punctuation token if c is punctuation
                }
            }
            if (charToString(c).matches(Type.STRING.FIRST_REGEX)) {
                tokens.add(scanString(c, program)); //add string token if c is "
            }
            if (charToString(c).matches(Type.NUMBER.FIRST_REGEX)) {
                tokens.add(scanNumber(c, program)); //add number token if c matches a number
            }
            if (charToString(c).matches(Type.SYMBOL.FIRST_REGEX)) {
                tokens.add(scanSymbol(c, program)); //add symbol token if c matches a symbol
            }
            if(tokens.size() == currTokens){
                throw new IllegalCharException(String.format("UNKNOWN CHAR '%c'", c));
            }
        }
        return tokens.iterator();
    }
}

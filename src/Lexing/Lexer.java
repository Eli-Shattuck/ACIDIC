package Lexing;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Types.*;
import Types.Exceptions.EndOfStringException;
import Types.Exceptions.IllegalCharException;

import static utils.Utils.charToString;
/*
 * class to lex an ACIDIC program into tokens, defined in Token.java
 */

public class Lexer {
    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a number, and returns it as a Token
     */
    private static Result<List<LexToken>> scanNumber(char first, CharacterIterator chars) {
        Result<List<LexToken>> result = new Result<>();
        StringBuilder ret = new StringBuilder(String.format("%c", first));
        int dotCount = first == '.' ? 1 : 0;
        while (chars.current() != CharacterIterator.DONE && charToString(chars.current()).matches(LexType.NUMBER.REST_REGEX)) {
            if(chars.current() == '.') dotCount++;
            ret.append(chars.current());
            chars.next();
        }
        if(dotCount == 0) {
            return result.success(Arrays.asList(new LexToken(LexType.INT, ret.toString())));
        } else if (dotCount == 1) {
            return result.success(Arrays.asList(new LexToken(LexType.FLOAT, ret.toString())));
        } else {
            return result.failure(new IllegalCharException("TOO MANY '.' IN NUMBER"));
        }
    }
    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a variable name, and returns it as a Token
     */
    private static Result<List<LexToken>> scanSymbol(char first, CharacterIterator chars) {
        Result<List<LexToken>> result = new Result<>();
        StringBuilder ret = new StringBuilder(String.format("%c", first));
        while (chars.current() != CharacterIterator.DONE && charToString(chars.current()).matches(LexType.SYMBOL.REST_REGEX)) {
            ret.append(chars.current());
            chars.next();
        }
        return result.success(Arrays.asList(new LexToken(LexType.SYMBOL, ret.toString())));
    }

    /*
     * takes in an iterator of chars, and the first element of that iterator
     * parses the iterator for a string, and returns it as a Token
     */
    private static Result<List<LexToken>> scanString(char first, CharacterIterator chars) {
        Result<List<LexToken>> result = new Result<>();
        StringBuilder ret = new StringBuilder();
        while (!charToString(chars.current()).matches(LexType.STRING.REST_REGEX)) {
            if (chars.current() == CharacterIterator.DONE)
                return result.failure(
                    new EndOfStringException("NO ' \" ' AT END OF STRING")
                );
            ret.append(chars.current());
            chars.next();
        }
        chars.next();
        return result.success(Arrays.asList(new LexToken(LexType.STRING, ret.toString())));
    }

    /*
     * takes in a program as a string and looks through the string one at a time,
     * lexing it into a list of Tokens
     */
    public static Result<List<LexToken>> lex(String programString) {
        CharacterIterator program = new StringCharacterIterator(programString);
        List<LexToken> lexTokens = new ArrayList<>();

        Result<List<LexToken>> res = new Result<>();

        while (program.current() != CharacterIterator.DONE) {
            char c = program.current();
            program.next();

            int currTokens = lexTokens.size();

            if ("\t\n ".indexOf(c) != -1) continue; //disregard any whitespace
            for(LexType op: LexType.getAllOperatorsList()) {
                if (charToString(c).matches(op.FIRST_REGEX)) {
                    lexTokens.add(new LexToken(op, null)); //add operation token if c is an operator
                }
            }
            for(LexType punc: LexType.getAllPunctuationList()) {
                if (charToString(c).matches(punc.FIRST_REGEX)) {
                    lexTokens.add(new LexToken(punc, null)); //add punctuation token if c is punctuation
                }
            }
            if (charToString(c).matches(LexType.STRING.FIRST_REGEX)) {
                lexTokens.addAll(res.register(scanString(c, program))); //add string token if c is "
                if(res.error()) return res;
            }
            if (charToString(c).matches(LexType.NUMBER.FIRST_REGEX)) {
                lexTokens.addAll(res.register(scanNumber(c, program))); //add number token if c matches a number
                if(res.error()) return res;
            }
            if (charToString(c).matches(LexType.SYMBOL.FIRST_REGEX)) {
                lexTokens.addAll(res.register(scanSymbol(c, program))); //add symbol token if c matches a symbol
                if(res.error()) return res;
            }
            if(lexTokens.size() == currTokens){
                return res.failure(new IllegalCharException(String.format("UNKNOWN CHAR '%c'", c)));
            }
        }
        return res.success(lexTokens);
    }
}

package Parsing;

import Types.*;
import Types.Exceptions.IllegalSyntaxException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/*
 * class that parses an iterator of Lex Tokens, into an expression tree,
 * following this grammar
 *
 * EXP --> TRM + TRM | TRM - TRM
 * TRM --> FAC * FAC | FAC / FAC
 * FAC --> +NUM | -NUM | INT | FLT | (EXP)
 * INT --> an in primate
 * FLT --> a floating point primitive
 */
public class Parser {
    private static HashSet<LexType> EXP_OPS = new HashSet<>(Arrays.asList(LexType.ADD, LexType.SUB));
    private static HashSet<LexType> TRM_OPS = new HashSet<>(Arrays.asList(LexType.MUL, LexType.DIV));
    private static HashSet<LexType> UNA_OPS = new HashSet<>(Arrays.asList(LexType.ADD, LexType.SUB));

    public static Result<ExpTreeNode> Parse(List<LexToken> lexTokens){
        PeekIterator<LexToken> pit = new PeekIterator<>(lexTokens);
        Result<ExpTreeNode> res = getEXP(pit);
        if(!res.error() && pit.getCurrent() != PeekIterator.END_OF_LIST) res.failure(new IllegalSyntaxException("EXPECTING OPERATOR"));
        return res;
    }

    private static Result<ExpTreeNode> getBinOp(PeekIterator<LexToken> lexTokens, Function<PeekIterator<LexToken>, Result<ExpTreeNode>> getter, Set<LexType> validLexTypes) {
        Result<ExpTreeNode> res = new Result<>();
        ExpTreeNode left = res.register(getter.apply(lexTokens));
        if (res.error()) return res;
        while(lexTokens.getCurrent() != PeekIterator.END_OF_LIST && validLexTypes.contains(lexTokens.getCurrent().getLexType())) {
            LexToken opLexToken = lexTokens.getCurrent();
            if(opLexToken == PeekIterator.END_OF_LIST) return res.failure(new IllegalSyntaxException("UNEXPECTED EOF"));
            lexTokens.next();
            ExpTreeNode right = res.register(getter.apply(lexTokens));
            if (res.error()) return res;
            left = new ExpTreeNode(opLexToken, InterpreterType.BIN_OPERATOR_NODE, left, right);
        }
        return res.success(left);
    }

    private static Result<ExpTreeNode> getEXP(PeekIterator<LexToken> lexTokens) {
        return getBinOp(lexTokens, Parser::getTRM, EXP_OPS);
    }

    private static Result<ExpTreeNode> getTRM(PeekIterator<LexToken> lexTokens) {
        return getBinOp(lexTokens, Parser::getFAC, TRM_OPS);
    }

    private static Result<ExpTreeNode> getFAC(PeekIterator<LexToken> lexTokens) {
        Result<ExpTreeNode> res = new Result<>();
        LexToken lexToken = lexTokens.getCurrent();
        if(lexToken == PeekIterator.END_OF_LIST) return res.failure(new IllegalSyntaxException("UNEXPECTED EOF"));
        if(UNA_OPS.contains(lexToken.getLexType())) {
            lexTokens.next();
            ExpTreeNode factor = res.register(getFAC(lexTokens));
            if(res.error()) return res;
            return res.success(new ExpTreeNode(lexToken, InterpreterType.UNARY_OPERATOR_NODE, factor));
        } else if(lexToken.getLexType() == LexType.INT || lexToken.getLexType() == LexType.FLOAT) {
            lexTokens.next();
            return res.success(new ExpTreeNode(lexToken, InterpreterType.NUMBER_NODE));
        } else if(lexToken.getLexType() == LexType.L_PAREN) {
            lexTokens.next();
            ExpTreeNode expression = res.register(getEXP(lexTokens));
            if(res.error()) return res;
            if(lexTokens.getCurrent().getLexType() == LexType.R_PAREN) {
                lexTokens.next();
                return res.success(expression);
            }
            return res.failure(new IllegalSyntaxException(String.format("EXPECTING TYPE %s INSTEAD FOUND TYPE %s", LexType.R_PAREN, lexTokens.getCurrent().getLexType())));
        }
        return res.failure(new IllegalSyntaxException(String.format("EXPECTING TYPE %s OR %s INSTEAD FOUND TYPE %s", LexType.INT, LexType.FLOAT, lexTokens.getCurrent().getLexType())));
    }
}

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
 * EXPRESSION   (EXP)
 * TERM         (TRM)
 * POWER        (POW)
 * FACTOR       (FAC)
 * KEYWORD      (KEY)
 * IDENTIFIER   (IDN)
 * ASSIGNMENT   (ASN)
 *
 * EXP --> KEY:DEF IDN ASN EXP | TRM + TRM | TRM - TRM | TRM
 * TRM --> POW * POW | POW / POW | +TRM | -TRM | POW
 * POW --> FAC ** POW | FAC
 * FAC --> INT | FLT | IDN | (EXP)
 */
public class Parser {
    private static HashSet<LexType> EXP_OPS = new HashSet<>(Arrays.asList(LexType.ADD, LexType.SUB));
    private static HashSet<LexType> TRM_OPS = new HashSet<>(Arrays.asList(LexType.MUL, LexType.DIV));
    private static HashSet<LexType> TRM_UNARY_OPS = new HashSet<>(Arrays.asList(LexType.ADD, LexType.SUB));
    private static HashSet<LexType> POW_OPS = new HashSet<>(Arrays.asList(LexType.POW));


    public static Result<ExpTreeNode> Parse(List<LexToken> lexTokens){
        PeekIterator<LexToken> pit = new PeekIterator<>(lexTokens);
        Result<ExpTreeNode> res = getEXP(pit);
        if(!res.error() && pit.getCurrent() != PeekIterator.END_OF_LIST) res.failure(new IllegalSyntaxException("EXPECTING OPERATOR"));
        return res;
    }

    private static Result<ExpTreeNode> getBinOp(PeekIterator<LexToken> lexTokens, Function<PeekIterator<LexToken>, Result<ExpTreeNode>> preGetter, Function<PeekIterator<LexToken>, Result<ExpTreeNode>> postGetter, Set<LexType> validLexTypes) {
        Result<ExpTreeNode> res = new Result<>();
        ExpTreeNode left = res.register(preGetter.apply(lexTokens));
        if (res.error()) return res;
        while(lexTokens.getCurrent() != PeekIterator.END_OF_LIST && validLexTypes.contains(lexTokens.getCurrent().getLexType())) {
            LexToken opLexToken = lexTokens.getCurrent();
            if(opLexToken == PeekIterator.END_OF_LIST) return res.failure(new IllegalSyntaxException("UNEXPECTED EOF"));
            lexTokens.next();
            ExpTreeNode right = res.register(postGetter.apply(lexTokens));
            if (res.error()) return res;
            left = new ExpTreeNode(opLexToken, InterpreterType.BIN_OPERATOR_NODE, left, right);
        }
        return res.success(left);
    }

    private static Result<ExpTreeNode> getEXP(PeekIterator<LexToken> lexTokens) {
        if(lexTokens.getCurrent().getLexType() == LexType.KEYWORD && lexTokens.getCurrent().getValue().equals(Keywords.DEF.toString())) { //if current token is the keyword 'DEF'
            Result<ExpTreeNode> res = new Result<>();
            if(lexTokens.next().getLexType() == LexType.SYMBOL) {
                LexToken symb = lexTokens.getCurrent();
                if(lexTokens.next().getLexType() == LexType.ASSIGNMENT) {
                    LexToken asn = lexTokens.getCurrent();
                    lexTokens.next();
                    ExpTreeNode val = res.register(getEXP(lexTokens));
                    if(res.error()) return res;
                    return res.success(
                            new ExpTreeNode(
                                    asn,
                                    InterpreterType.VAR_ASSIGNMENT_NODE,
                                    new ExpTreeNode(symb, InterpreterType.SYMBOL_NODE),
                                    val
                            )
                    );
                } else {
                    return res.failure(new IllegalSyntaxException(String.format("EXPECTED %s AFTER %s", LexType.ASSIGNMENT, LexType.SYMBOL)));
                }
            } else {
                return res.failure(new IllegalSyntaxException(String.format("EXPECTED %s AFTER %s:%s", LexType.SYMBOL, LexType.KEYWORD, Keywords.DEF)));
            }
        }

        return getBinOp(lexTokens, Parser::getTRM, Parser::getTRM, EXP_OPS);
    }

    private static Result<ExpTreeNode> getTRM(PeekIterator<LexToken> lexTokens) {
        if(TRM_UNARY_OPS.contains(lexTokens.getCurrent().getLexType())) {
            Result<ExpTreeNode> res = new Result<>();
            LexToken c = lexTokens.getCurrent();
            lexTokens.next();
            ExpTreeNode factor = res.register(getTRM(lexTokens));
            if(res.error()) return res;
            return res.success(new ExpTreeNode(c, InterpreterType.UNARY_OPERATOR_NODE, factor));
        }
        return getBinOp(lexTokens, Parser::getPOW, Parser::getPOW, TRM_OPS);
    }

    private static Result<ExpTreeNode> getPOW(PeekIterator<LexToken> lexTokens) {
        return getBinOp(lexTokens, Parser::getFAC, Parser::getPOW, POW_OPS);
    }

    private static Result<ExpTreeNode> getFAC(PeekIterator<LexToken> lexTokens) {
        Result<ExpTreeNode> res = new Result<>();
        LexToken lexToken = lexTokens.getCurrent();
        if(lexToken == PeekIterator.END_OF_LIST) return res.failure(new IllegalSyntaxException("UNEXPECTED EOF"));
        if(lexToken.getLexType() == LexType.INT || lexToken.getLexType() == LexType.FLOAT) {
            lexTokens.next();
            return res.success(new ExpTreeNode(lexToken, InterpreterType.NUMBER_NODE));
        } else if(lexToken.getLexType() == LexType.SYMBOL) {
            lexTokens.next();
            return res.success(new ExpTreeNode(lexToken, InterpreterType.VAR_ACCESS_NODE));
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

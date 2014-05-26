package parser;

import lexer.Lexer;
import lexer.Token;
import model.*;

import java.util.LinkedList;
import java.util.List;

public class Parser {
    private LinkedList<Token> tokens = new LinkedList<>();
    public int afterLastTokenPos;
    public Integer lastCheckedTokenPos = null;
    public Integer errorPos = null;

    public Exp parse(String input) {
        setInitialValues();
        Lexer lexer = new Lexer();
        lexer.tokenize(input);
        tokens.clear();
        tokens.addAll(lexer.getTokensWithNoWhitespaces());
        if(this.tokens.size() == 0) {
            return null;
        }
        afterLastTokenPos = this.tokens.getLast().position + this.tokens.getLast().value.length();
        return parseStmt();
    }

    public Exp parse(List<Token> tokens) {
        setInitialValues();
        this.tokens.clear();
        this.tokens.addAll(tokens);
        if(this.tokens.size() == 0) {
            return null;
        }
        afterLastTokenPos = this.tokens.getLast().position + this.tokens.getLast().value.length();
        return parseStmt();
    }

    private void setInitialValues() {
        lastCheckedTokenPos = -1;
        errorPos = -1;
    }

    private Exp parseStmt() {
        Exp res;
        if(tokens.size() >= 2 && tokens.get(0).type == Token.Type.ID && tokens.get(1).type == Token.Type.ASSIGNMENT) {
            res = parseAssignment();
        } else {
            res = parseExpr();
        }
        if(res != null && tokens.size() != 0) {
            errorPos = tokens.getFirst().position;
            ++lastCheckedTokenPos;
            return null;
        }
        return res;
    }

    private Exp parseAssignment() {
        Token first = nextToken();
        nextToken();
        Exp expr = parseExpr();
        return expr == null ? null : new Assign(first.value, expr);
    }

    private Exp parseExpr() {
        if(tokens.isEmpty()) {
            errorPos = afterLastTokenPos;
            lastCheckedTokenPos = -1;
            return null;
        }
        Exp term = parseTerm();
        if(term == null) { return null; }
        if(!tokens.isEmpty()) {
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.PLUS_OP)) == 0) {
                nextToken();
                Exp rightTerm = parseExpr();
                return rightTerm == null ? null : new Sum(term, rightTerm);
            }
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.MINUS_OP)) == 0) {
                nextToken();
                Exp rightTerm = parseExpr();
                return rightTerm == null ? null : new Substr(term, rightTerm);
            }
        }
        return term;
    }

    private Exp parseTerm() {
        if(tokens.isEmpty()) {
            errorPos = afterLastTokenPos;
            lastCheckedTokenPos = -1;
            return null;
        }
        Exp fact = parseFact();
        if(fact == null) { return null; }
        if(!tokens.isEmpty()) {
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.MUL_OP)) == 0) {
                nextToken();
                Exp rightFact = parseTerm();
                return rightFact == null ? null : new Mul(fact, rightFact);
            }
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.DIV_OP)) == 0) {
                nextToken();
                Exp rightFact = parseTerm();
                return rightFact == null ? null : new Div(fact, rightFact);
            }
        }
        return fact;
    }

    private Exp parseFact() {
        if(tokens.isEmpty()) {
            errorPos = afterLastTokenPos;
            lastCheckedTokenPos = -1;
            return null;
        }
        Token first = nextToken();
        if(isNegativeNumber(first)) {
            return new Num(Integer.parseInt(first.value + nextToken().value));
        }
        if(first.type == Token.Type.NUMBER) { return new Num(Integer.parseInt(first.value)); }
        if(first.type == Token.Type.ID) { return new Var(first.value); }
        if(first.type == Token.Type.OPEN_PAR) {
            Exp expr = parseExpr();
            if(expr == null) { return null; }
            if(tokens.isEmpty()) {
                errorPos = afterLastTokenPos;
                lastCheckedTokenPos = -1;
                return null;
            }
            if(tokens.getFirst().type != Token.Type.CLOSE_PAR) {
                errorPos = tokens.getFirst().position;
                ++lastCheckedTokenPos;
                return null;
            }
            nextToken();
            return expr;
        }
        errorPos = first.position;
        return null;
    }

    private Token nextToken() {
        Token first = tokens.removeFirst();
        ++lastCheckedTokenPos;
        return first;
    }

    private boolean isNegativeNumber(Token first) {
        return !tokens.isEmpty() && first.type == Token.Type.OPERATOR &&
               first.value.compareTo(Character.toString(Lexer.MINUS_OP)) == 0 &&
               tokens.get(0).type == Token.Type.NUMBER;
    }
}
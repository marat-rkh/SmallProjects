package parser;

import lexer.Lexer;
import lexer.Token;
import model.Assign;
import model.Exp;
import model.Mul;
import model.Sum;
import model.Div;
import model.Var;
import model.Num;

import java.util.LinkedList;

public class Parser {
    private Lexer lexer = new Lexer();
    private LinkedList<Token> tokens = new LinkedList<>();
    private int inputAfterLastPos;

    public int errorPos;

    public Exp parse(String input) {
        init(input);
        return parseStmt();
    }

    private void init(String input) {
        errorPos = -1;
        inputAfterLastPos = input.length();
        tokens.clear();
        tokens.addAll(lexer.tokenize(input));
    }

    private Exp parseStmt() {
        Exp res;
        if(tokens.size() > 2 && tokens.get(1).type == Token.Type.ASSIGNMENT) {
            res = parseAssignment();
        } else {
            res = parseExpr();
        }
        if(tokens.size() != 0) {
            errorPos = tokens.getFirst().position;
            return null;
        }
        return res;
    }

    private Exp parseAssignment() {
        Token first = tokens.removeFirst();
        if(first.type != Token.Type.ID) {
            errorPos = first.position;
            return null;
        }
        tokens.removeFirst();
        Exp expr = parseExpr();
        return expr == null ? null : new Assign(first.value, expr);
    }

    private Exp parseExpr() {
        if(tokens.isEmpty()) {
            errorPos = inputAfterLastPos;
            return null;
        }
        Exp term = parseTerm();
        if(term == null) { return null; }
        if(!tokens.isEmpty()) {
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.PLUS_OP)) == 0) {
                tokens.removeFirst();
                Exp rightTerm = parseExpr();
                return rightTerm == null ? null : new Sum(term, rightTerm);
            }
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.MINUS_OP)) == 0) {
                tokens.removeFirst();
                Exp rightTerm = parseTerm();
                return rightTerm == null ? null : new Sum(term, new Mul(new Num(-1), rightTerm));
            }
        }
        return term;
    }

    private Exp parseTerm() {
        if(tokens.isEmpty()) {
            errorPos = inputAfterLastPos;
            return null;
        }
        Exp fact = parseFact();
        if(fact == null) { return null; }
        if(!tokens.isEmpty()) {
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.MUL_OP)) == 0) {
                tokens.removeFirst();
                Exp rightFact = parseTerm();
                return rightFact == null ? null : new Mul(fact, rightFact);
            }
            if (tokens.getFirst().value.compareTo(Character.toString(Lexer.DIV_OP)) == 0) {
                tokens.removeFirst();
                Exp rightFact = parseFact();
                return rightFact == null ? null : new Div(fact, rightFact);
            }
        }
        return fact;
    }

    private Exp parseFact() {
        if(tokens.isEmpty()) {
            errorPos = inputAfterLastPos;
            return null;
        }
        Token first = tokens.removeFirst();
        if(first.type == Token.Type.NUMBER) { return new Num(Integer.parseInt(first.value)); }
        if(first.type == Token.Type.ID) { return new Var(first.value); }
        if(first.type == Token.Type.OPEN_PAR) {
            Exp expr = parseExpr();
            if(expr == null) { return null; }
            if(tokens.isEmpty()) {
                errorPos = inputAfterLastPos;
                return null;
            }
            if(tokens.getFirst().type != Token.Type.CLOSE_PAR) {
                errorPos = tokens.getFirst().position;
                return null;
            }
            tokens.removeFirst();
            return expr;
        }
        errorPos = first.position;
        return null;
    }
}
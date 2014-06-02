package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    public LexState state;
    public static final char PLUS_OP = '+';
    public static final char MINUS_OP = '-';
    public static final char MUL_OP = '*';
    public static final char DIV_OP = '/';
    protected final Map<Character, Token.Type> singleLetter;
    {
        singleLetter = new HashMap<>();
        singleLetter.put(PLUS_OP, Token.Type.OPERATOR);
        singleLetter.put(MINUS_OP, Token.Type.OPERATOR);
        singleLetter.put(MUL_OP, Token.Type.OPERATOR);
        singleLetter.put(DIV_OP, Token.Type.OPERATOR);
        singleLetter.put('(', Token.Type.OPEN_PAR);
        singleLetter.put(')', Token.Type.CLOSE_PAR);
        singleLetter.put(' ', Token.Type.WHITESPACE);
        singleLetter.put('=', Token.Type.ASSIGNMENT);
    }
    List<Token> tokens = null;

    public void tokenize(String input) {
        state = new GeneralState(this);
        String normInput = normalizeInput(input);
        tokens = new ArrayList<>();
        for(int i = 0; i < normInput.length(); i++) {
            state.processNext(getType(normInput.charAt(i)), normInput.charAt(i), i, tokens);
        }
        Token last = state.getCurrentToken();
        if(last != null) {
            tokens.add(last);
        }
    }

    public List<Token> getAllTokens() { return tokens; }
    public List<Token> getTokensWithNoWhitespaces() {
        List<Token> filtered = new ArrayList<>();
        for (Token token : tokens) {
            if(token.type != Token.Type.WHITESPACE) {
                filtered.add(token);
            }
        }
        return filtered;
    }

    private Token.Type getType(char symbol) {
        if(Character.isDigit(symbol)) {
            return Token.Type.NUMBER;
        }
        if(Character.isLetter(symbol)) {
            return Token.Type.ID;
        }
        Token.Type type = singleLetter.get(symbol);
        return type != null ? type : Token.Type.UNKNOWN;
    }

    private String normalizeInput(String input) {
//        return input.replaceAll(" +", " ");
        return input;
    }
}

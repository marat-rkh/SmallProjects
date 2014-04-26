package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    public LexState state = new GeneralState(this);
    protected final Map<Character, Token.Type> singleLetter;
    {
        singleLetter = new HashMap<>();
        singleLetter.put('+', Token.Type.OPERATOR);
        singleLetter.put('-', Token.Type.OPERATOR);
        singleLetter.put('*', Token.Type.OPERATOR);
        singleLetter.put('/', Token.Type.OPERATOR);
        singleLetter.put('(', Token.Type.OPEN_PAR);
        singleLetter.put(')', Token.Type.CLOSE_PAR);
        singleLetter.put(' ', Token.Type.WHITESPACE);
        singleLetter.put('=', Token.Type.ASSIGNMENT);
    }

    public List<Token> tokenize(String input) {
        String normInput = normalizeInput(input);
        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < normInput.length(); i++) {
            state.processNext(getType(normInput.charAt(i)), normInput.charAt(i), i, tokens);
        }
        Token last = state.getCurrentToken();
        if(last != null) {
            tokens.add(last);
        }
        return tokens;
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
        return input.trim().replaceAll(" +", " ");
    }
}

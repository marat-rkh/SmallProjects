package lexer;

import java.util.List;

public class UnknownState extends LexState {
    public UnknownState(Lexer lexer) {
        super(lexer);
    }

    public UnknownState(Lexer lexer, String value, int pos) {
        super(lexer);
        currentValue = value;
        position = pos;
    }

    @Override
    public void processNext(Token.Type type, char symbol, int pos, List<Token> tokens) {
        switch (type) {
            case NUMBER:
            case ID:
            case UNKNOWN:
                if(currentValue.isEmpty()) {
                    position = pos;
                }
                currentValue += symbol;
                break;
            case WHITESPACE:
            case OPEN_PAR:
            case CLOSE_PAR:
            case OPERATOR:
            case ASSIGNMENT:
                tokens.add(new Token(Token.Type.UNKNOWN, currentValue, position));
                lexer.state = new GeneralState(lexer);
                lexer.state.processNext(type, symbol, pos, tokens);
        }
    }

    @Override
    public Token getCurrentToken() { return new Token(Token.Type.UNKNOWN, currentValue, position); }
}

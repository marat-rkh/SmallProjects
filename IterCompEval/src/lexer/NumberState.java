package lexer;

import java.util.List;

public class NumberState extends LexState {
    public NumberState(Lexer lexer) {
        super(lexer);
    }

    @Override
    public void processNext(Token.Type type, char symbol, int pos, List<Token> tokens) {
        switch (type) {
            case NUMBER:
                if(currentValue.isEmpty()) {
                    position = pos;
                }
                currentValue += symbol;
                break;
            case ID:
            case UNKNOWN:
                lexer.state = new UnknownState(lexer, currentValue + symbol, position);
                break;
            case WHITESPACE:
            case OPEN_PAR:
            case CLOSE_PAR:
            case OPERATOR:
            case ASSIGNMENT:
                tokens.add(new Token(Token.Type.NUMBER, currentValue, position));
                lexer.state = new GeneralState(lexer);
                lexer.state.processNext(type, symbol, pos, tokens);
        }
    }

    @Override
    public Token getCurrentToken() { return new Token(Token.Type.NUMBER, currentValue, position); }
}

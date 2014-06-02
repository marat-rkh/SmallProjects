package lexer;

import java.util.List;

public class IdState extends LexState {
    public IdState(Lexer lexer) {
        super(lexer);
    }

    @Override
    public void processNext(Token.Type type, char symbol, int pos, List<Token> tokens) {
        switch (type) {
            case ID:
                if(currentValue.isEmpty()) {
                    position = pos;
                }
                currentValue += symbol;
                break;
            case NUMBER:
                if(currentValue.isEmpty()) {
                    lexer.state = new UnknownState(lexer, "" + symbol, pos);
                } else {
                    currentValue += symbol;
                }
                break;
            case UNKNOWN:
                lexer.state = new UnknownState(lexer, currentValue + symbol, position);
                break;
            case WHITESPACE:
            case OPEN_PAR:
            case CLOSE_PAR:
            case OPERATOR:
            case ASSIGNMENT:
                tokens.add(new Token(Token.Type.ID, currentValue, position));
                lexer.state = new GeneralState(lexer);
                lexer.state.processNext(type, symbol, pos, tokens);
        }
    }

    @Override
    public Token getCurrentToken() { return new Token(Token.Type.ID, currentValue, position); }
}

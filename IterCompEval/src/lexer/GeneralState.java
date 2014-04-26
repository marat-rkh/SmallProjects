package lexer;

import java.util.List;

public class GeneralState extends LexState {
    public GeneralState(Lexer lexer) {
        super(lexer);
    }

    @Override
    public void processNext(Token.Type type, char symbol, int pos, List<Token> tokens) {
        switch (type) {
            case NUMBER:
                lexer.state = new NumberState(lexer);
                lexer.state.processNext(type, symbol, pos, tokens);
                break;
            case ID:
                lexer.state = new IdState(lexer);
                lexer.state.processNext(type, symbol, pos, tokens);
                break;
            case UNKNOWN:
                lexer.state = new UnknownState(lexer);
                lexer.state.processNext(type, symbol, pos, tokens);
                break;
            case WHITESPACE:
            case OPEN_PAR:
            case CLOSE_PAR:
            case OPERATOR:
            case ASSIGNMENT:
                tokens.add(new Token(type, Character.toString(symbol), pos));
                break;
        }
    }

    @Override
    public Token getCurrentToken() { return null; }
}

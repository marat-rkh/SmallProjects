package lexer;

import java.util.List;

abstract public class LexState {
    protected Lexer lexer;
    protected String currentValue = "";
    protected int position = 0;

    public LexState(Lexer lexer) { this.lexer = lexer; }
    abstract public void processNext(Token.Type type, char symbol, int pos, List<Token> tokens);
    abstract public Token getCurrentToken();
}
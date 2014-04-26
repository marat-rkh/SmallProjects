package lexer;

public class Token {
    public enum Type { NUMBER, ID, OPERATOR, OPEN_PAR, CLOSE_PAR, ASSIGNMENT, WHITESPACE, UNKNOWN };

    public Type type;
    public String value;
    public int position;

    public Token(Type type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }
}

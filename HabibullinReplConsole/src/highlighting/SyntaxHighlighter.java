package highlighting;

import lexer.Lexer;
import lexer.Token;
import model.Exp;
import parser.Parser;

import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SyntaxHighlighter {
    public static final String EMPTY_STYLE_NAME = "EmptyStyle";
    public static final String OPERAND_STYLE_NAME = "OperandStyle";
    public static final String OPERATOR_STYLE_NAME = "OperatorStyle";
    public static final String ERROR_STYLE_NAME = "ErrorStyle";

    private List<String> stylesMap = new ArrayList<>();

    public List<String> getStylesMap(String input, Map<String, Exp> context) throws BadLocationException {
        stylesMap.clear();
        if(input.length() != 0) {
            Lexer lexer = new Lexer();
            lexer.tokenize(input);
            Parser parser = new Parser();
            Exp rootExp = parser.parse(lexer.getTokensWithNoWhitespaces());
            int lastTokenToHighlight = lastCorrectTokenPos(lexer.getAllTokens(), parser, rootExp);
            fillMapForValidPart(lexer.getAllTokens(), lastTokenToHighlight, context);
            if (rootExp == null) {
                fillMapForInvalidPart(lexer.getAllTokens(), lastTokenToHighlight);
            }
        }
        return stylesMap;
    }

    private int lastCorrectTokenPos(List<Token> allTokens, Parser parser, Exp rootExp) {
        int lastTokenToHighlight = allTokens.size() - 1;
        if (rootExp == null) {
            if(parser.errorPos == parser.afterLastTokenPos) {
                while (allTokens.get(lastTokenToHighlight).type == Token.Type.WHITESPACE) {
                    --lastTokenToHighlight;
                }
            } else {
                int checkedNotWhitespaceTokenPos = -1;
                int shift = 0;
                for (Token token : allTokens) {
                    if (token.type == Token.Type.WHITESPACE) {
                        ++shift;
                    } else {
                        ++checkedNotWhitespaceTokenPos;
                    }
                    if (checkedNotWhitespaceTokenPos == parser.lastCheckedTokenPos) {
                        break;
                    }
                }
                lastTokenToHighlight = parser.lastCheckedTokenPos - 1 + shift;
            }
        }
        return lastTokenToHighlight;
    }

    private void fillMapForValidPart(List<Token> tokens, int lastTokenToHighlight, Map<String, Exp> context)
            throws BadLocationException
    {
        for (int i = 0; i <= lastTokenToHighlight; i++) {
            Token.Type type = tokens.get(i).type;
            if(type == Token.Type.NUMBER) {
                fillMapPart(tokens.get(i).value.length(), OPERAND_STYLE_NAME);
            } else if(type == Token.Type.ID) {
                if(context != null && !context.containsKey(tokens.get(i).value)) {
                    fillMapPart(tokens.get(i).value.length(), ERROR_STYLE_NAME);
                } else {
                    fillMapPart(tokens.get(i).value.length(), OPERAND_STYLE_NAME);
                }
            } else if(type == Token.Type.OPERATOR) {
                fillMapPart(tokens.get(i).value.length(), OPERATOR_STYLE_NAME);
            } else {
                fillMapPart(tokens.get(i).value.length(), EMPTY_STYLE_NAME);
            }
        }
    }

    private void fillMapForInvalidPart(List<Token> tokens, int lastTokenToHighlight) throws BadLocationException {
        if(lastTokenToHighlight + 1 < tokens.size()) {
            for (int i = lastTokenToHighlight + 1; i < tokens.size(); i++) {
                fillMapPart(tokens.get(i).value.length(), ERROR_STYLE_NAME);
            }
        } else {
            stylesMap.add(ERROR_STYLE_NAME);
        }
    }

    private void fillMapPart(int size, String styleName) {
        for (int i = 0; i < size; i++) {
            stylesMap.add(styleName);
        }
    }
}
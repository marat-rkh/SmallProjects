package inputprocessing;

import lexer.Lexer;
import lexer.Token;
import model.Exp;
import parser.Parser;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.util.List;
import java.util.Map;

public class SyntaxHighlighter {
    private JTextPane textPane;

    private Style operandStyle;
    private Style operatorStyle;
    private Style errorStyle;

    private final String OPERAND_STYLE_NAME = "OperandStyle";
    private final String OPERATOR_STYLE_NAME = "OperatorStyle";
    private final String ERROR_STYLE_NAME = "ErrorStyle";

    public SyntaxHighlighter(JTextPane textPane) {
        this.textPane = textPane;
        operandStyle = textPane.addStyle(OPERAND_STYLE_NAME, null);
        StyleConstants.setForeground(operandStyle, Color.GREEN);
        operatorStyle = textPane.addStyle(OPERATOR_STYLE_NAME, null);
        StyleConstants.setForeground(operatorStyle, Color.BLUE);
        errorStyle = textPane.addStyle(ERROR_STYLE_NAME, null);
        StyleConstants.setForeground(errorStyle, Color.RED);
        StyleConstants.setUnderline(errorStyle, true);
    }

    public void printHighlighted(String input, StyledDocument doc, int insertPos, int caretPos, Map<String, Exp> context)
            throws BadLocationException
    {
        Lexer lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getAllTokens();
        int lastTokenToHighlight = tokens.size() - 1;
        Parser parser = new Parser();
        Exp rootExp = parser.parse(input, lexer.getTokensWithNoWhitespaces());
        if(rootExp == null) {
            lastTokenToHighlight = parser.lastCheckedTokenPos;
        }
        insertPos = printValidPart(doc, insertPos, tokens, lastTokenToHighlight, context);
        if(rootExp == null) {
            printInvalidPart(doc, insertPos, tokens, lastTokenToHighlight);
        }
        textPane.setCaretPosition(caretPos);
    }

    private int printValidPart(StyledDocument doc, int insertPos, List<Token> tokens, int lastTokenToHighlight,
                               Map<String, Exp> context) throws BadLocationException
    {
        for (int i = 0; i <= lastTokenToHighlight; i++) {
            Token.Type type = tokens.get(i).type;
            if(type == Token.Type.NUMBER) {
                doc.insertString(insertPos, tokens.get(i).value, operandStyle);
            } else if(type == Token.Type.ID) {
                if(context != null && !context.containsKey(tokens.get(i).value)) {
                    doc.insertString(insertPos, tokens.get(i).value, errorStyle);
                } else {
                    doc.insertString(insertPos, tokens.get(i).value, operandStyle);
                }
            } else if(type == Token.Type.OPERATOR) {
                doc.insertString(insertPos, tokens.get(i).value, operatorStyle);
            } else {
                doc.insertString(insertPos, tokens.get(i).value, null);
            }
            insertPos += tokens.get(i).value.length();
        }
        return insertPos;
    }

    private void printInvalidPart(StyledDocument doc, int insertPos, List<Token> tokens, int lastTokenToHighlight)
            throws BadLocationException
    {
        if(lastTokenToHighlight + 1 < tokens.size()) {
            for (int i = lastTokenToHighlight + 1; i < tokens.size(); i++) {
                doc.insertString(insertPos, tokens.get(i).value, errorStyle);
                insertPos += tokens.get(i).value.length();
            }
        } else {
            doc.insertString(insertPos, " ", errorStyle);
        }
    }
}

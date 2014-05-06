package gui;

import inputprocessing.SyntaxHighlighter;
import model.Assign;
import model.Exp;
import model.Num;
import parser.Parser;
import visitors.ExpStringPrinter;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class REPLConsole {
    private final String SIMPLIFY = "Simplify";
    private final String EVALUATE = "Evaluate";
    private final String GREETING = System.lineSeparator() + ">";

    private JFrame frame;
    private final JComboBox<String> optionPane = new JComboBox<>();
    {
        optionPane.addItem(SIMPLIFY);
        optionPane.addItem(EVALUATE);
    }

    private Map<String, Exp> context = new HashMap<>();

    public void init() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(optionPane, "North");

        JTextPane textArea = new JTextPane();
        AbstractDocument document = (AbstractDocument) textArea.getDocument();
        document.setDocumentFilter(new Filter());
        textArea.setText("Welcome to REPL Console! " + System.lineSeparator() + ">");
        textArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, "Center");

        textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JTextPane source = (JTextPane) event.getSource();
                Document document = source.getDocument();
                try {
                    String text = document.getText(0, document.getLength());
                    String userInput = text.substring(lastLineIndex(document) + GREETING.length());
                    String result = "Evaluation result from user input: " +
                            evaluate(userInput, simplifyModeSelected(optionPane));
                    document.insertString(endOffset(document), System.lineSeparator() + result, null);
                    document.insertString(endOffset(document), GREETING, null);
                    source.setCaretPosition(endOffset(document));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }

            private int endOffset(Document document) {
                return document.getEndPosition().getOffset() - 1;
            }
        });
        textArea.addKeyListener(textAreaKeyAdapter);

        frame.setVisible(true);
        frame.setSize(500, 300);
    }

    private boolean simplifyModeSelected(JComboBox<String> optionPane) {
        return SIMPLIFY.equals(optionPane.getSelectedItem());
    }

    private KeyAdapter textAreaKeyAdapter = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
            char symbol = keyEvent.getKeyChar();
            JTextPane source = (JTextPane) keyEvent.getSource();
            try {
                if(charIsTypingSymbol(symbol)) {
                    handleSymbolTypedEvent(source, keyEvent);
                }
            } catch (BadLocationException e) {
                System.out.println("Bad location while typing. Details: " + e.getMessage());
            }
        }
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();
            JTextPane source = (JTextPane) keyEvent.getSource();
            try {
                if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_KP_LEFT) {
                    handleKeyLeftArrowEvent(source, keyEvent);
                } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_KP_RIGHT) {
                    handleKeyRightArrowEvent(source, keyEvent);
                } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_KP_UP ||
                        keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_KP_DOWN)
                {
                    keyEvent.consume();
                } else if (keyCode == KeyEvent.VK_BACK_SPACE && caretInEditableArea(source, -1)) {
                    handleBackspacePressedEvent(source, keyEvent);
                }
            } catch (BadLocationException e) {
                System.out.println("Bad location while typing. Details: " + e.getMessage());
            }
        }
    };

    private String evaluate(String userInput, boolean simplifyMode) {
        Parser parser = new Parser();
        Exp root = parser.parse(userInput);
        if(root == null) {
            return "error at pos " + parser.errorPos;
        }
        Exp result = root.evaluate(context);
        if(!simplifyMode && !(result instanceof Num)) {
            return "expression contains undefined variables";
        }
        ExpStringPrinter expStringPrinter = new ExpStringPrinter();
        result.accept(expStringPrinter);
        String resultStringRepr = expStringPrinter.flushBuffer();
        return (root instanceof Assign) ? ((Assign) root).name + " = " + resultStringRepr : resultStringRepr;
    }

    private boolean charIsTypingSymbol(char symbol) {
        String allowedSymbols = "+/()*= ";
        return Character.isLetterOrDigit(symbol) || allowedSymbols.indexOf(symbol) != -1;
    }

    private boolean caretInEditableArea(JTextPane textPane, int shift) throws BadLocationException {
        StyledDocument document = textPane.getStyledDocument();
        String text = document.getText(0, document.getLength());
        int editableAreaStart = lastLineIndex(document) + GREETING.length();
        String currentUserInput = text.substring(editableAreaStart);
        int editableAreaEnd = editableAreaStart + currentUserInput.length();
        int currentCaretPos = textPane.getCaretPosition() + shift;
        return currentCaretPos >= editableAreaStart && currentCaretPos <= editableAreaEnd;
    }

    private void handleKeyLeftArrowEvent(JTextPane textPane, KeyEvent keyEvent) throws BadLocationException {
        if(!caretInEditableArea(textPane, -1)) {
            keyEvent.consume();
        }
    }

    private void handleKeyRightArrowEvent(JTextPane textPane, KeyEvent keyEvent) throws BadLocationException {
        if(!caretInEditableArea(textPane, 1)) {
            keyEvent.consume();
        }
    }

    private void handleSymbolTypedEvent(JTextPane textPane, KeyEvent keyEvent) throws BadLocationException {
        char symbol = keyEvent.getKeyChar();
        int resultingPos = textPane.getCaretPosition() + 1;
        String userInput = getUserInputWithAddedSym(textPane, symbol);
        insertStringHighlighted(textPane, userInput, resultingPos);
        keyEvent.consume();
    }

    private void handleBackspacePressedEvent(JTextPane textPane, KeyEvent keyEvent) throws BadLocationException {
        int resultingPos = textPane.getCaretPosition() - 1;
        String userInput = getUserInputWithDeletedSym(textPane);
        insertStringHighlighted(textPane, userInput, resultingPos);
        keyEvent.consume();
    }

    private String getUserInputWithAddedSym(JTextPane textPane, char symbolAdded) throws BadLocationException {
        StyledDocument document = textPane.getStyledDocument();
        String text = document.getText(0, document.getLength());
        int oldUserInputZeroPos = lastLineIndex(document) + GREETING.length();
        String oldUserInput = text.substring(oldUserInputZeroPos);
        int currentCaretPos = textPane.getCaretPosition() - oldUserInputZeroPos;
        return oldUserInput.substring(0, currentCaretPos) + symbolAdded + oldUserInput.substring(currentCaretPos);
    }

    private String getUserInputWithDeletedSym(JTextPane textPane) throws BadLocationException {
        StyledDocument document = textPane.getStyledDocument();
        String text = document.getText(0, document.getLength());
        int oldUserInputZeroPos = lastLineIndex(document) + GREETING.length();
        String oldUserInput = text.substring(oldUserInputZeroPos);
        int currentCaretPos = textPane.getCaretPosition() - oldUserInputZeroPos;
        return oldUserInput.substring(0, currentCaretPos - 1) + oldUserInput.substring(currentCaretPos);
    }

    private void insertStringHighlighted(JTextPane textPane, String userInput, int resultingPos) throws BadLocationException {
        StyledDocument doc = textPane.getStyledDocument();
        String text = doc.getText(0, doc.getLength());
        String oldUserInput = text.substring(lastLineIndex(doc) + GREETING.length());
        doc.remove(endOffset(doc) - oldUserInput.length(), oldUserInput.length());
        SyntaxHighlighter syntaxHighlighter = new SyntaxHighlighter(textPane);
        if(!userInput.isEmpty()) {
            if(simplifyModeSelected(optionPane)) {
                syntaxHighlighter.printHighlighted(userInput, doc, endOffset(doc), resultingPos, null);
            } else {
                syntaxHighlighter.printHighlighted(userInput, doc, endOffset(doc), resultingPos, context);
            }
        }
    }

    private int endOffset(Document document) {
        return document.getEndPosition().getOffset() - 1;
    }

    private class Filter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (cursorOnLastLine(offset, fb)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
            if (offset > lastLineIndex(fb.getDocument()) + 1) {
                super.remove(fb, offset, length);
            }
        }

        public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
                throws BadLocationException {
            if (cursorOnLastLine(offset, fb)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

    }

    private boolean cursorOnLastLine(int offset, DocumentFilter.FilterBypass fb) {
        return cursorOnLastLine(offset, fb.getDocument());
    }

    private boolean cursorOnLastLine(int offset, Document document) {
        int lastLineIndex;
        try {
            lastLineIndex = lastLineIndex(document);
        } catch (BadLocationException e) {
            return false;
        }
        return offset > lastLineIndex;
    }

    private int lastLineIndex(Document document) throws BadLocationException {
        return document.getText(0, document.getLength()).lastIndexOf(System.lineSeparator());
    }
}
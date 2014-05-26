package gui;

import highlighting.SyntaxHighlighter;
import model.Assign;
import model.Exp;
import model.Num;
import parser.Parser;
import replcommands.AssignCommand;
import replcommands.EvalCommand;
import replcommands.REPLCommand;
import visitors.ExpStringPrinter;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.List;

public class REPLConsole {
    private final String SIMPLIFY = "Simplify";
    private final String EVALUATE = "Evaluate";
    private final String GREETING = System.lineSeparator() + ">";

    private final JComboBox<String> optionPane = new JComboBox<>();
    private UndoManager undoManager = new UndoManager();

    private Map<String, Exp> context = new HashMap<>();
    private Stack<REPLCommand> commandHistory = new Stack<>();

    public void init() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        addOptionPane(frame);

        JTextPane textPane = new JTextPane();
        setDocStyles(textPane);
        textPane.setText("Welcome to REPL Console! " + System.lineSeparator() + ">");

        textPane.getDocument().addUndoableEditListener(undoManager);
        ((AbstractDocument)textPane.getDocument()).setDocumentFilter(new DocHighlightingFilter(textPane));

        textPane.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), evaluateAction);
        textPane.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("control Z"), undoTextAction);
        textPane.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("control shift Z"), undoCommandAction);

        textPane.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.add(scrollPane, "Center");

        frame.setVisible(true);
        frame.setSize(500, 300);
    }

    private void addOptionPane(JFrame frame) {
        optionPane.addItem(SIMPLIFY);
        optionPane.addItem(EVALUATE);
        frame.add(optionPane, "North");
    }

    private void setDocStyles(JTextPane textPane) {
        textPane.addStyle(SyntaxHighlighter.EMPTY_STYLE_NAME, null);
        Style operandStyle = textPane.addStyle(SyntaxHighlighter.OPERAND_STYLE_NAME, null);
        StyleConstants.setForeground(operandStyle, Color.GREEN);
        StyleConstants.setBold(operandStyle, true);
        Style operatorStyle = textPane.addStyle(SyntaxHighlighter.OPERATOR_STYLE_NAME, null);
        StyleConstants.setForeground(operatorStyle, Color.BLUE);
        Style errorStyle = textPane.addStyle(SyntaxHighlighter.ERROR_STYLE_NAME, null);
        StyleConstants.setForeground(errorStyle, Color.RED);
        StyleConstants.setUnderline(errorStyle, true);
    }

    private AbstractAction evaluateAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent event) {
            JTextPane source = (JTextPane) event.getSource();
            Document document = source.getDocument();
            try {
                String text = document.getText(0, document.getLength());
                String userInput = text.substring(lastLineIndex(document) + GREETING.length());
                String result = "Evaluation result: " +
                        evaluate(userInput, simplifyModeSelected());
                document.insertString(endOffset(document), System.lineSeparator() + result, null);
                document.insertString(endOffset(document), GREETING, null);
                source.setCaretPosition(endOffset(document));
                undoManager.discardAllEdits();
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    };

    private String evaluate(String userInput, boolean simplifyMode) {
        Parser parser = new Parser();
        Exp root = parser.parse(userInput);
        if(root == null) {
            return "error at pos " + parser.errorPos;
        }
        Exp result;
        try {
            result = root.evaluate(context);
        } catch (Exception e) {
            return e.getMessage();
        }
        if(simplifyMode || (result instanceof Num)) {
            ExpStringPrinter expStringPrinter = new ExpStringPrinter();
            result.accept(expStringPrinter);
            String resultStringRepr = expStringPrinter.flushBuffer();
            if (root instanceof Assign) {
                String varName = ((Assign) root).name;
                resultStringRepr = varName + " = " + resultStringRepr;
                commandHistory.push(new AssignCommand(varName));
            } else {
                commandHistory.push(new EvalCommand());
            }
            return resultStringRepr;
        }
        commandHistory.push(new EvalCommand());
        return "expression contains undefined variables";
    }

    private AbstractAction undoTextAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(undoManager.canUndo()) {
                try {
                    undoManager.undo();
                } catch (CannotUndoException e) {
                    System.out.println("Undo error. Details: " + e.getMessage());
                }
            }
        }
    };

    private AbstractAction undoCommandAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(!commandHistory.empty()) {
                REPLCommand lastCommand = commandHistory.pop();
                if(lastCommand instanceof AssignCommand) {
                    String varName = ((AssignCommand) lastCommand).getVarName();
                    context.remove(varName);
                }
                try {
                    removeLastInputOutput((JTextPane)actionEvent.getSource());
                    undoManager.discardAllEdits();
                } catch (BadLocationException e) {
                    System.out.println("Bad location. Details: " + e.getMessage());
                }
            }
        }
    };

    private void removeLastInputOutput(JTextPane textPane) throws BadLocationException {
        StyledDocument doc = textPane.getStyledDocument();
        String text = doc.getText(0, doc.getLength());
        int delIndex = text.lastIndexOf(GREETING);
        int removingTextLength = text.length() - delIndex;
        text = text.substring(0, delIndex);
        delIndex = text.lastIndexOf(GREETING) + GREETING.length();
        removingTextLength += (text.length() - delIndex);
        text = text.substring(0, delIndex);
        ((AbstractDocument)doc).setDocumentFilter(null);
        doc.remove(text.length(), removingTextLength);
        ((AbstractDocument)doc).setDocumentFilter(new DocHighlightingFilter(textPane));
    }

    private boolean simplifyModeSelected() {
        return SIMPLIFY.equals(optionPane.getSelectedItem());
    }

    private int endOffset(Document document) {
        return document.getEndPosition().getOffset() - 1;
    }

    private int lastLineIndex(Document document) throws BadLocationException {
        return document.getText(0, document.getLength()).lastIndexOf(System.lineSeparator());
    }

    private class DocHighlightingFilter extends DocumentFilter {
        private JTextPane textPane;
        private boolean highlightingOn = true;

        private DocHighlightingFilter(JTextPane textPane) {
            this.textPane = textPane;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (cursorInEditableArea(offset, fb)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
            if (cursorInEditableArea(offset, fb)) {
                super.remove(fb, offset, length);
                if(highlightingOn) {
                    highlightInput();
                }
            }
        }
        @Override
        public void replace(final FilterBypass fb, final int offset, final int insLength, final String text, final AttributeSet attrs)
                throws BadLocationException {
            if (cursorInEditableArea(offset, fb)) {
                super.replace(fb, offset, insLength, text, attrs);
                if(highlightingOn) {
                    highlightInput();
                }
            }
        }

        private void highlightInput() throws BadLocationException {
            StyledDocument styledDocument = textPane.getStyledDocument();
            String userInput = getUserInput(styledDocument);
            if(userInput.length() != 0) {
                List<String> stylesMap = getStylesMap(userInput);
                int insertPos = lastLineIndex(styledDocument) + GREETING.length();
                int caretPos = textPane.getCaretPosition();
                styledDocument.removeUndoableEditListener(undoManager);
                int i = 0;
                for (; i < userInput.length(); i++) {
                    styledDocument.setCharacterAttributes(insertPos, 1, styledDocument.getStyle(stylesMap.get(i)), true);
                    ++insertPos;
                }
                for (; i < stylesMap.size(); i++) {
                    styledDocument.insertString(insertPos, " ", styledDocument.getStyle(stylesMap.get(i)));
                    ++insertPos;
                }
                textPane.setCaretPosition(caretPos);
                styledDocument.addUndoableEditListener(undoManager);
            }
        }

        private String getUserInput(Document doc) throws BadLocationException {
            String fullText = doc.getText(0, doc.getLength());
            int userInputZeroPos = fullText.lastIndexOf(System.lineSeparator()) + GREETING.length();
            return fullText.substring(userInputZeroPos);
        }

        private List<String> getStylesMap(String userInput) throws BadLocationException {
            SyntaxHighlighter syntaxHighlighter = new SyntaxHighlighter();
            List<String> stylesMap;
            if(simplifyModeSelected()) {
                stylesMap = syntaxHighlighter.getStylesMap(userInput, null);
            } else {
                stylesMap = syntaxHighlighter.getStylesMap(userInput, context);
            }
            return stylesMap;
        }
    }

    private boolean cursorInEditableArea(int offset, DocumentFilter.FilterBypass fb) {
        Document document = fb.getDocument();
        int lastLineIndex;
        try {
            lastLineIndex = lastLineIndex(document);
        } catch (BadLocationException e) {
            return false;
        }
        return offset > lastLineIndex + 1;
    }
}
package gui;

public class REPLConsoleOld {
//    private JFrame frame;
//    private JComboBox<String> optionPane;
//    private UndoManager undoManager = new UndoManager();
//
//    public static final String SIMPLIFY = "Simplify";
//    public static final String EVALUATE = "Evaluate";
//    public static final String GREETING = System.lineSeparator() + ">";
//
//    private Map<String, Exp> context = new HashMap<>();
//    private Stack<REPLCommand> commandHistory = new Stack<>();
//
//    private StyledDocument setupStyles() {
//        StyledDocument styledDocument = new DefaultStyledDocument();
//        styledDocument.addStyle(SyntaxHighlighter.EMPTY_STYLE_NAME, null);
//        Style operandStyle = styledDocument.addStyle(SyntaxHighlighter.OPERAND_STYLE_NAME, null);
//        StyleConstants.setForeground(operandStyle, Color.GREEN);
//        StyleConstants.setBold(operandStyle, true);
//        Style operatorStyle = styledDocument.addStyle(SyntaxHighlighter.OPERATOR_STYLE_NAME, null);
//        StyleConstants.setForeground(operatorStyle, Color.BLUE);
//        Style errorStyle = styledDocument.addStyle(SyntaxHighlighter.ERROR_STYLE_NAME, null);
//        StyleConstants.setForeground(errorStyle, Color.RED);
//        StyleConstants.setUnderline(errorStyle, true);
//        return styledDocument;
//    }
//
//    public void init() {
//        frame = new JFrame();
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosed(WindowEvent e) {
//                System.exit(0);
//            }
//        });
//        frame.setLayout(new BorderLayout());
//
//        optionPane = new JComboBox<>();
//        optionPane.addItem(SIMPLIFY);
//        optionPane.addItem(EVALUATE);
//        frame.add(optionPane, "North");
//
//        final JTextPane textArea = new JTextPane(setupStyles());
//        final AbstractDocument document = (AbstractDocument) textArea.getDocument();
//
//        textArea.setText("Welcome to REPL Console! " + System.lineSeparator() + ">");
//        document.addUndoableEditListener(undoManager);
//        document.setDocumentFilter(new Filter((StyledDocument) document));
//        textArea.setEditable(true);
//        frame.add(textArea, "Center");
//
//        textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("control Z"), new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(undoManager.canUndo()) {
//                    undoManager.undo();
//                }
//            }
//        });
//
//        textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("control shift Z"), new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (commandHistory.empty()) {
//                    return;
//                }
//                undo();
//                removeLines();
//            }
//
//            private void removeLines() {
//                try {
//                    String text = document.getText(0, document.getLength());
//                    int prevToLastLineIndex = text.lastIndexOf(System.lineSeparator());
//                    text = text.substring(0, prevToLastLineIndex);
//                    int indexToTrim = text.lastIndexOf('>') + 1;
//
//                    document.setDocumentFilter(null);
//                    document.remove(indexToTrim, document.getLength() - indexToTrim);
//                    document.setDocumentFilter(new Filter((StyledDocument) document));
//                } catch (BadLocationException e1) {
//                    //Ignored
//                }
//            }
//
//            private void undo() {
//                commandHistory.pop();
//            }
//        });
//
//        textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent event) {
//                JTextPane source = (JTextPane) event.getSource();
//                Document document = source.getDocument();
//                try {
//                    String text = document.getText(0, document.getLength());
//                    String userInput = text.substring(lastLineIndex(document) + GREETING.length());
//                    String result = "Evaluation result: " +
//                            evaluate(userInput, simplifyModeSelected());
//                    document.insertString(endOffset(document), System.lineSeparator() + result, null);
//                    document.insertString(endOffset(document), GREETING, null);
//                    source.setCaretPosition(endOffset(document));
//                    undoManager.discardAllEdits();
//                } catch (BadLocationException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            private int endOffset(Document document) {
//                return document.getEndPosition().getOffset() - 1;
//            }
//
//            public boolean simplifyModeSelected() {
//                return SIMPLIFY.equals(optionPane.getSelectedItem());
//            }
//        });
//
//        frame.setVisible(true);
//        frame.setSize(500, 300);
//    }
//
//    private String evaluate(String userInput, boolean simplifyMode) {
//        Parser parser = new Parser();
//        Exp root = parser.parse(userInput);
//        if(root == null) {
//            return "error at pos " + parser.errorPos;
//        }
//        Exp result;
//        try {
//            result = root.evaluate(context);
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//        if(simplifyMode || (result instanceof Num)) {
//            ExpStringPrinter expStringPrinter = new ExpStringPrinter();
//            result.accept(expStringPrinter);
//            String resultStringRepr = expStringPrinter.flushBuffer();
//            if (root instanceof Assign) {
//                String varName = ((Assign) root).name;
//                resultStringRepr = varName + " = " + resultStringRepr;
//                commandHistory.push(new AssignCommand(varName));
//            } else {
//                commandHistory.push(new EvalCommand());
//            }
//            return resultStringRepr;
//        }
//        return "expression contains undefined variables";
//    }
//
//    private class Filter extends DocumentFilter {
//        public Filter(StyledDocument styledDocument) {
//            this.styledDocument = styledDocument;
//        }
//
//        @Override
//        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
//            if (cursorOnLastLine(offset, fb)) {
//                super.insertString(fb, offset, string, attr);
//                highlightInput();
//            }
//        }
//
//        public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
//            if (offset > lastLineIndex(fb.getDocument()) + 1) {
//                super.remove(fb, offset, length);
//                highlightInput();
//            }
//        }
//
//        public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
//                throws BadLocationException {
//            if (cursorOnLastLine(offset, fb)) {
//                super.replace(fb, offset, length, text, attrs);
//                highlightInput();
//            }
//        }
//
//        public void highlightInput() throws BadLocationException {
//            styledDocument.removeUndoableEditListener(undoManager);
//            String userInput = getUserInput(styledDocument);
//            List<String> stylesMap = getStylesMap(userInput);
//            int insertPos = lastLineIndex(styledDocument) + GREETING.length();
//
//            for(String styleName : stylesMap) {
//                System.out.println("insertPos: " + insertPos);
//                styledDocument.setCharacterAttributes(insertPos, 1, styledDocument.getStyle(styleName), true);
//                ++insertPos;
//            }
//            styledDocument.addUndoableEditListener(undoManager);
//        }
//
//        private String getUserInput(Document doc) throws BadLocationException {
//            String fullText = doc.getText(0, doc.getLength());
//            int userInputZeroPos = fullText.lastIndexOf(System.lineSeparator()) + GREETING.length();
//            System.out.println("userInputZeroPos: " + userInputZeroPos);
//            return fullText.substring(userInputZeroPos);
//        }
//
//        private List<String> getStylesMap(String userInput) throws BadLocationException {
//            SyntaxHighlighter syntaxHighlighter = new SyntaxHighlighter();
//            List<String> stylesMap;
//            if(simplifyModeSelected()) {
//                stylesMap = syntaxHighlighter.getStylesMap(userInput, null);
//            } else {
//                stylesMap = syntaxHighlighter.getStylesMap(userInput, context);
//            }
//            return stylesMap;
//        }
//
//        public boolean simplifyModeSelected() {
//            return SIMPLIFY.equals(optionPane.getSelectedItem());
//        }
//
//        private final StyledDocument styledDocument;
//    }
//
//    private static boolean cursorOnLastLine(int offset, DocumentFilter.FilterBypass fb) {
//        return cursorOnLastLine(offset, fb.getDocument());
//    }
//
//    private static boolean cursorOnLastLine(int offset, Document document) {
//        int lastLineIndex;
//        try {
//            lastLineIndex = lastLineIndex(document);
//        } catch (BadLocationException e) {
//            return false;
//        }
//        return offset > lastLineIndex;
//    }
//
//    public static int lastLineIndex(Document document) throws BadLocationException {
//        return document.getText(0, document.getLength()).lastIndexOf(System.lineSeparator());
//    }
}
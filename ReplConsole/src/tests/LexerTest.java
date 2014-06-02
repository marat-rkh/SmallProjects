//package tests;
//
//import lexer.Lexer;
//import lexer.Token;
//import org.junit.Assert;
//
//import java.util.List;
//
//public class LexerTest {
//    @org.junit.Test
//    public void testTokenize() throws Exception {
//        Lexer lexer = new Lexer();
//        String input = "y = 5 +x / 133 * (2 %  z123      (1e#- ))";
//        lexer.tokenize(input);
//        List<Token> tokens = lexer.getTokensWithNoWhitespaces();
//        Assert.assertTrue(tokens.size() == 17);
//        Assert.assertTrue(tokens.get(0).value.compareTo("y") == 0 && tokens.get(0).type == Token.Type.ID
//                && tokens.get(0).position == 0);
//        Assert.assertTrue(tokens.get(1).value.compareTo("=") == 0 && tokens.get(1).type == Token.Type.ASSIGNMENT
//                && tokens.get(1).position == 2);
//        Assert.assertTrue(tokens.get(2).value.compareTo("5") == 0 && tokens.get(2).type == Token.Type.NUMBER
//                && tokens.get(2).position == 4);
//        Assert.assertTrue(tokens.get(3).value.compareTo("+") == 0 && tokens.get(3).type == Token.Type.OPERATOR
//                && tokens.get(3).position == 6);
//        Assert.assertTrue(tokens.get(4).value.compareTo("x") == 0 && tokens.get(4).type == Token.Type.ID
//                && tokens.get(4).position == 7);
//        Assert.assertTrue(tokens.get(5).value.compareTo("/") == 0 && tokens.get(5).type == Token.Type.OPERATOR
//                && tokens.get(5).position == 9);
//        Assert.assertTrue(tokens.get(6).value.compareTo("133") == 0 && tokens.get(6).type == Token.Type.NUMBER
//                && tokens.get(6).position == 11);
//        Assert.assertTrue(tokens.get(7).value.compareTo("*") == 0 && tokens.get(7).type == Token.Type.OPERATOR
//                && tokens.get(7).position == 15);
//        Assert.assertTrue(tokens.get(8).value.compareTo("(") == 0 && tokens.get(8).type == Token.Type.OPEN_PAR
//                && tokens.get(8).position == 17);
//        Assert.assertTrue(tokens.get(9).value.compareTo("2") == 0 && tokens.get(9).type == Token.Type.NUMBER
//                && tokens.get(9).position == 18);
//        Assert.assertTrue(tokens.get(10).value.compareTo("%") == 0 && tokens.get(10).type == Token.Type.UNKNOWN
//                && tokens.get(10).position == 20);
//        Assert.assertTrue(tokens.get(11).value.compareTo("z123") == 0 && tokens.get(11).type == Token.Type.ID
//                && tokens.get(11).position == 23);
//        Assert.assertTrue(tokens.get(12).value.compareTo("(") == 0 && tokens.get(12).type == Token.Type.OPEN_PAR
//                && tokens.get(12).position == 33);
//        Assert.assertTrue(tokens.get(13).value.compareTo("1e#") == 0 && tokens.get(13).type == Token.Type.UNKNOWN
//                && tokens.get(13).position == 34);
//        Assert.assertTrue(tokens.get(14).value.compareTo("-") == 0 && tokens.get(14).type == Token.Type.OPERATOR
//                && tokens.get(14).position == 37);
//        Assert.assertTrue(tokens.get(15).value.compareTo(")") == 0 && tokens.get(15).type == Token.Type.CLOSE_PAR
//                && tokens.get(15).position == 39);
//        Assert.assertTrue(tokens.get(16).value.compareTo(")") == 0 && tokens.get(16).type == Token.Type.CLOSE_PAR
//                && tokens.get(16).position == 40);
//    }
//}

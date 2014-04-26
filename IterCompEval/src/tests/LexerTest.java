package tests;

import lexer.Lexer;
import lexer.Token;
import org.junit.Assert;

import java.util.List;

public class LexerTest {
    @org.junit.Test
    public void testTokenize() throws Exception {
        Lexer lexer = new Lexer();
        String input = "y = 5 +x / 133 * (2 %  z123      (1e#- ))";
        List<Token> tokens = lexer.tokenize(input);
        Assert.assertTrue(tokens.size() == 28);
        Assert.assertTrue(tokens.get(0).value.equals("y") && tokens.get(0).type == Token.Type.ID
                && tokens.get(0).position == 0);
        Assert.assertTrue(tokens.get(1).value.equals(" ") && tokens.get(1).type == Token.Type.WHITESPACE
                && tokens.get(1).position == 1);
        Assert.assertTrue(tokens.get(2).value.equals("=") && tokens.get(2).type == Token.Type.ASSIGNMENT
                && tokens.get(2).position == 2);
        Assert.assertTrue(tokens.get(3).value.equals(" ") && tokens.get(3).type == Token.Type.WHITESPACE
                && tokens.get(3).position == 3);
        Assert.assertTrue(tokens.get(4).value.equals("5") && tokens.get(4).type == Token.Type.NUMBER
                && tokens.get(4).position == 4);
        Assert.assertTrue(tokens.get(5).value.equals(" ") && tokens.get(5).type == Token.Type.WHITESPACE
                && tokens.get(5).position == 5);
        Assert.assertTrue(tokens.get(6).value.equals("+") && tokens.get(6).type == Token.Type.OPERATOR
                && tokens.get(6).position == 6);
        Assert.assertTrue(tokens.get(7).value.equals("x") && tokens.get(7).type == Token.Type.ID
                && tokens.get(7).position == 7);
        Assert.assertTrue(tokens.get(8).value.equals(" ") && tokens.get(8).type == Token.Type.WHITESPACE
                && tokens.get(8).position == 8);
        Assert.assertTrue(tokens.get(9).value.equals("/") && tokens.get(9).type == Token.Type.OPERATOR
                && tokens.get(9).position == 9);
        Assert.assertTrue(tokens.get(10).value.equals(" ") && tokens.get(10).type == Token.Type.WHITESPACE
                && tokens.get(10).position == 10);
        Assert.assertTrue(tokens.get(11).value.equals("133") && tokens.get(11).type == Token.Type.NUMBER
                && tokens.get(11).position == 11);
        Assert.assertTrue(tokens.get(12).value.equals(" ") && tokens.get(12).type == Token.Type.WHITESPACE
                && tokens.get(12).position == 14);
        Assert.assertTrue(tokens.get(13).value.equals("*") && tokens.get(13).type == Token.Type.OPERATOR
                && tokens.get(13).position == 15);
        Assert.assertTrue(tokens.get(14).value.equals(" ") && tokens.get(14).type == Token.Type.WHITESPACE
                && tokens.get(14).position == 16);
        Assert.assertTrue(tokens.get(15).value.equals("(") && tokens.get(15).type == Token.Type.OPEN_PAR
                && tokens.get(15).position == 17);
        Assert.assertTrue(tokens.get(16).value.equals("2") && tokens.get(16).type == Token.Type.NUMBER
                && tokens.get(16).position == 18);
        Assert.assertTrue(tokens.get(17).value.equals(" ") && tokens.get(17).type == Token.Type.WHITESPACE
                && tokens.get(17).position == 19);
        Assert.assertTrue(tokens.get(18).value.equals("%") && tokens.get(18).type == Token.Type.UNKNOWN
                && tokens.get(18).position == 20);
        Assert.assertTrue(tokens.get(19).value.equals(" ") && tokens.get(19).type == Token.Type.WHITESPACE
                && tokens.get(19).position == 21);
        Assert.assertTrue(tokens.get(20).value.equals("z123") && tokens.get(20).type == Token.Type.ID
                && tokens.get(20).position == 22);
        Assert.assertTrue(tokens.get(21).value.equals(" ") && tokens.get(21).type == Token.Type.WHITESPACE
                && tokens.get(21).position == 26);
        Assert.assertTrue(tokens.get(22).value.equals("(") && tokens.get(22).type == Token.Type.OPEN_PAR
                && tokens.get(22).position == 27);
        Assert.assertTrue(tokens.get(23).value.equals("1e#") && tokens.get(23).type == Token.Type.UNKNOWN
                && tokens.get(23).position == 28);
        Assert.assertTrue(tokens.get(24).value.equals("-") && tokens.get(24).type == Token.Type.OPERATOR
                && tokens.get(24).position == 31);
        Assert.assertTrue(tokens.get(25).value.equals(" ") && tokens.get(25).type == Token.Type.WHITESPACE
                && tokens.get(25).position == 32);
        Assert.assertTrue(tokens.get(26).value.equals(")") && tokens.get(26).type == Token.Type.CLOSE_PAR
                && tokens.get(26).position == 33);
        Assert.assertTrue(tokens.get(27).value.equals(")") && tokens.get(27).type == Token.Type.CLOSE_PAR
                && tokens.get(27).position == 34);
    }
}

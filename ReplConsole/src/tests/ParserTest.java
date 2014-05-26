package tests;

import org.junit.Test;
import org.junit.Assert;

import model.Exp;
import parser.Parser;

public class ParserTest {
    @Test
    public void testParse() throws Exception {
        String input = "x = 33";
        Parser parser = new Parser();
        Exp root = parser.parse(input);
        Assert.assertTrue(root != null);

        input = "2 + (33 * 2 * 2)";
        root = parser.parse(input);
        Assert.assertTrue(root != null);

        input = "2 + (33 * 2 /(1 + 2 - 3)) - 5 * 15";
        root = parser.parse(input);
        Assert.assertTrue(root != null);

        input = "x = 33 %";
        root = parser.parse(input);
        Assert.assertTrue(root == null);
        Assert.assertTrue(parser.errorPos == input.length() - 1);
        Assert.assertTrue(parser.lastCheckedTokenPos == 3);

        input = "2 + (33 * )";
        root = parser.parse(input);
        Assert.assertTrue(root == null);
        Assert.assertTrue(parser.errorPos == input.length() - 1);
        Assert.assertTrue(parser.lastCheckedTokenPos == 5);

        input = "2 + (33 * 2";
        root = parser.parse(input);
        Assert.assertTrue(root == null);
        Assert.assertTrue(parser.errorPos == input.length());
    }
}

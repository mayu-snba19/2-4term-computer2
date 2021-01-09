import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;
import java.util.*;

class ParseExpressionTest {
    @Test
    @DisplayName("parseExpressionのテスト（次のトークンは';'）")
    public void test_parseExpression() {
        String src = "x;";
        Parser p = makeParser(src);
        p.parseExpression();
        String actual = p.getPrintedString();
        String expected =
        "expression\n"+
        "    term\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'x'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseExpressionのテスト2（次のトークンは';'）")
    public void test_parseExpression2() {
        String src = "x + y;";
        Parser p = makeParser(src);
        p.parseExpression();
        String actual = p.getPrintedString();
        String expected =
        "expression\n"+
        "    term\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'x'\n"+
        "    op\n"+
        "        '+'\n"+
        "    term\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'y'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseExpressionのテスト3（次のトークンは';'）")
    public void test_parseExpression3() {
        String src = "x + y + z;";
        Parser p = makeParser(src);
        p.parseExpression();
        String actual = p.getPrintedString();
        String expected =
        "expression\n"+
        "    term\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'x'\n"+
        "    op\n"+
        "        '+'\n"+
        "    term\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'y'\n"+
        "    op\n"+
        "        '+'\n"+
        "    term\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'z'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（integerConstant）のテスト")
    public void test_parseTerm() {
        String src = "123";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    integerConstant\n"+
        "        '123'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（stringConstant）のテスト")
    public void test_parseTerm2() {
        String src = "\"hello\"";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    stringConstant\n"+
        "        '\"hello\"'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（keywordConstant）のテスト")
    public void test_parseTerm3() {
        String src = "true";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    keywordConstant\n"+
        "        'true'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（varName）のテスト（次のトークンは';'）")
    public void test_parseTerm4() {
        String src = "x1;";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'x1'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（配列要素）のテスト")
    public void test_parseTerm5() {
        String src = "a[0]";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'a'\n"+
        "    '['\n"+
        "    expression\n"+
        "        term\n"+
        "            integerConstant\n"+
        "                '0'\n"+
        "    ']'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（subroutineCall）のテスト")
    public void test_parseTerm6() {
        String src = "f()";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    subroutineCall\n"+
        "        subroutineName\n"+
        "            identifier\n"+
        "                'f'\n"+
        "        '('\n"+
        "        expressionList\n"+
        "        ')'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（かっこ）のテスト")
    public void test_parseTerm7() {
        String src = "(x)";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    '('\n"+
        "    expression\n"+
        "        term\n"+
        "            varName\n"+
        "                identifier\n"+
        "                    'x'\n"+
        "    ')'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTerm（unaryOp）のテスト（次のトークンは';'）")
    public void test_parseTerm8() {
        String src = "-x;";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    unaryOp\n"+
        "        '-'\n"+
        "    term\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'x'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseSubroutinCall（単純名）のテスト")
    public void test_parseSubroutineCall() {
        String src = "f()";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    subroutineCall\n"+
        "        subroutineName\n"+
        "            identifier\n"+
        "                'f'\n"+
        "        '('\n"+
        "        expressionList\n"+
        "        ')'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseSubroutinCall（限定名）のテスト")
    public void test_parseSubroutineCall2() {
        String src = "x.f()";
        Parser p = makeParser(src);
        p.parseTerm();
        String actual = p.getPrintedString();
        String expected =
        "term\n"+
        "    subroutineCall\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'x'\n"+
        "        '.'\n"+
        "        subroutineName\n"+
        "            identifier\n"+
        "                'f'\n"+
        "        '('\n"+
        "        expressionList\n"+
        "        ')'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseExpressionList（0個）のテスト（次のトークンは')'）")
    public void test_parseExpressionList0() {
        //### String src = "";
        String src = ")";
        Parser p = makeParser(src);
        p.parseExpressionList();
        String actual = p.getPrintedString();
        String expected =
        "expressionList\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseExpressionList（1個）のテスト（次のトークンは')'）")
    public void test_parseExpressionList1() {
        String src = "x)";
        Parser p = makeParser(src);
        p.parseExpressionList();
        String actual = p.getPrintedString();
        String expected =
        "expressionList\n"+
        "    expression\n"+
        "        term\n"+
        "            varName\n"+
        "                identifier\n"+
        "                    'x'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseExpressionList（3個）のテスト（次のトークンは')'）")
    public void test_parseExpressionList3() {
        String src = "x,y,z)";
        Parser p = makeParser(src);
        p.parseExpressionList();
        String actual = p.getPrintedString();
        String expected =
        "expressionList\n"+
        "    expression\n"+
        "        term\n"+
        "            varName\n"+
        "                identifier\n"+
        "                    'x'\n"+
        "    ','\n"+
        "    expression\n"+
        "        term\n"+
        "            varName\n"+
        "                identifier\n"+
        "                    'y'\n"+
        "    ','\n"+
        "    expression\n"+
        "        term\n"+
        "            varName\n"+
        "                identifier\n"+
        "                    'z'\n";
        assertEquals(expected, actual, src);
    }
    String[] ops = {"+", "-", "*", "/", "&", "|", "<", ">", "="};
    @Test
    @DisplayName("parseOpのテスト")
    public void test_parseOp() {
        for (String op: ops) {
            String src = op;
            Parser p = makeParser(src);
            p.parseOp();
            String actual = p.getPrintedString();
            String expected =
            "op\n"+
            "    '"+op+"'\n";
            assertEquals(expected, actual, src);
        }
    }
    String[] unaryOps = {"-", "~"};
    @Test
    @DisplayName("parseUnaryOpのテスト")
    public void test_parseUnaryOp() {
        for (String op: unaryOps) {
            String src = op;
            Parser p = makeParser(src);
            p.parseUnaryOp();
            String actual = p.getPrintedString();
            String expected =
            "unaryOp\n"+
            "    '"+op+"'\n";
            assertEquals(expected, actual, src);
        }
    }
    String[] keywordConstants = {"true", "false", "null", "this"};
    @Test
    @DisplayName("parseKeywordConstantのテスト")
    public void test_parseKeywordConstant() {
        for (String kc: keywordConstants) {
            String src = kc;
            Parser p = makeParser(src);
            p.parseKeywordConstant();
            String actual = p.getPrintedString();
            String expected =
            "keywordConstant\n"+
            "    '"+kc+"'\n";
            assertEquals(expected, actual, src);
        }
    }
    Parser makeParser(String src) {
        //List<Token> tokens = new ArrayList<Token>();
        Tokenizer t = new Tokenizer();
        //t.tokenize(src, tokens);
        List<Token> tokens = t.tokenize(src);
        return new Parser(tokens,true);
    }
}
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;
import java.util.*;

class ParseStatementTest {
    @Test
    @DisplayName("parseLetStatementのテスト")
    public void test_parseLetStatement() {
        String src = "let x = 1;";
        Parser p = makeParser(src);
        p.parseLetStatement();
        String actual = p.getPrintedString();
        String expected = 
        "letStatement\n"+
        "    'let'\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'x'\n"+
        "    '='\n"+
        "    expression\n"+
        "        term\n"+
        "            integerConstant\n"+
        "                '1'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseLetStatementのテスト2")
    public void test_parseLetStatement2() {
        String src = "let x[0] = 1;";
        Parser p = makeParser(src);
        p.parseLetStatement();
        String actual = p.getPrintedString();
        String expected = 
        "letStatement\n"+
        "    'let'\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'x'\n"+
        "    '['\n"+
        "    expression\n"+
        "        term\n"+
        "            integerConstant\n"+
        "                '0'\n"+
        "    ']'\n"+
        "    '='\n"+
        "    expression\n"+
        "        term\n"+
        "            integerConstant\n"+
        "                '1'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseIfStatementのテスト（次のトークンは'}'）")
    public void test_parseIfStatement() {
        String src = "if (true) { } }";
        Parser p = makeParser(src);
        p.parseIfStatement();
        String actual = p.getPrintedString();
        String expected =
        "ifStatement\n"+
        "    'if'\n"+
        "    '('\n"+
        "    expression\n"+
        "        term\n"+
        "            keywordConstant\n"+
        "                'true'\n"+
        "    ')'\n"+
        "    '{'\n"+
        "    statements\n"+
        "    '}'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseIfStatementのテスト2")
    public void test_parseIfStatement2() {
        String src = "if (true) { } else { }";
        Parser p = makeParser(src);
        p.parseIfStatement();
        String actual = p.getPrintedString();
        String expected =
        "ifStatement\n"+
        "    'if'\n"+
        "    '('\n"+
        "    expression\n"+
        "        term\n"+
        "            keywordConstant\n"+
        "                'true'\n"+
        "    ')'\n"+
        "    '{'\n"+
        "    statements\n"+
        "    '}'\n"+
        "    'else'\n"+
        "    '{'\n"+
        "    statements\n"+
        "    '}'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseWhileStatementのテスト")
    public void test_parseWhileStatement() {
        String src = "while (true) { }";
        Parser p = makeParser(src);
        p.parseWhileStatement();
        String actual = p.getPrintedString();
        String expected =
        "whileStatement\n"+
        "    'while'\n"+
        "    '('\n"+
        "    expression\n"+
        "        term\n"+
        "            keywordConstant\n"+
        "                'true'\n"+
        "    ')'\n"+
        "    '{'\n"+
        "    statements\n"+
        "    '}'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseDoStatementのテスト")
    public void test_parseDoStatement() {
        String src = "do f();";
        Parser p = makeParser(src);
        p.parseDoStatement();
        String actual = p.getPrintedString();
        String expected =
        "doStatement\n"+
        "    'do'\n"+
        "    subroutineCall\n"+
        "        subroutineName\n"+
        "            identifier\n"+
        "                'f'\n"+
        "        '('\n"+
        "        expressionList\n"+
        "        ')'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseReturnStatementのテスト")
    public void test_parseReturnStatement() {
        String src = "return;";
        Parser p = makeParser(src);
        p.parseReturnStatement();
        String actual = p.getPrintedString();
        String expected =
        "returnStatement\n"+
        "    'return'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseReturnStatementのテスト2")
    public void test_parseReturnStatement2() {
        String src = "return true;";
        Parser p = makeParser(src);
        p.parseReturnStatement();
        String actual = p.getPrintedString();
        String expected =
        "returnStatement\n"+
        "    'return'\n"+
        "    expression\n"+
        "        term\n"+
        "            keywordConstant\n"+
        "                'true'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    Parser makeParser(String src) {
        //List<Token> tokens = new ArrayList<Token>();
        Tokenizer t = new Tokenizer();
        //t.tokenize(src, tokens);
        List<Token> tokens = t.tokenize(src);
        return new Parser(tokens,true);
    }
}
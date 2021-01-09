import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;
import java.util.*;

class ParseClassTest {
    @Test
    @DisplayName("parseClassのテスト")
    public void test_parseClass() {
        String src = "class Main { }";
        Parser p = makeParser(src);
        p.parseClass();
        String actual = p.getPrintedString();
        String expected = 
        "class\n"+
        "    'class'\n"+
        "    className\n"+
        "        identifier\n"+
        "            'Main'\n"+
        "    '{'\n"+
        "    '}'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseClassのテスト2")
    public void test_parseClass2() {
        String src = "class Main { static int s; field boolean f; function void main() { } method int m(int a) { var int x; } }";
        Parser p = makeParser(src);
        p.parseClass();
        String actual = p.getPrintedString();
        String expected = 
        "class\n"+
        "    'class'\n"+
        "    className\n"+
        "        identifier\n"+
        "            'Main'\n"+
        "    '{'\n"+
        "    classVarDec\n"+
        "        'static'\n"+
        "        type\n"+
        "            'int'\n"+
        "        varName\n"+
        "            identifier\n"+
        "                's'\n"+
        "        ';'\n"+
        "    classVarDec\n"+
        "        'field'\n"+
        "        type\n"+
        "            'boolean'\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'f'\n"+
        "        ';'\n"+
        "    subroutineDec\n"+
        "        'function'\n"+
        "        'void'\n"+
        "        subroutineName\n"+
        "            identifier\n"+
        "                'main'\n"+
        "        '('\n"+
        "        parameterList\n"+
        "        ')'\n"+
        "        subroutineBody\n"+
        "            '{'\n"+
        "            statements\n"+
        "            '}'\n"+
        "    subroutineDec\n"+
        "        'method'\n"+
        "        type\n"+
        "            'int'\n"+
        "        subroutineName\n"+
        "            identifier\n"+
        "                'm'\n"+
        "        '('\n"+
        "        parameterList\n"+
        "            type\n"+
        "                'int'\n"+
        "            varName\n"+
        "                identifier\n"+
        "                    'a'\n"+
        "        ')'\n"+
        "        subroutineBody\n"+
        "            '{'\n"+
        "            varDec\n"+
        "                'var'\n"+
        "                type\n"+
        "                    'int'\n"+
        "                varName\n"+
        "                    identifier\n"+
        "                        'x'\n"+
        "                ';'\n"+
        "            statements\n"+
        "            '}'\n"+
        "    '}'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseClassVarDecのテスト")
    public void test_parseClassVarDec() {
        String src = "static int x;";
        Parser p = makeParser(src);
        p.parseClassVarDec();
        String actual = p.getPrintedString();
        String expected = 
        "classVarDec\n"+
        "    'static'\n"+
        "    type\n"+
        "        'int'\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'x'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseClassVarDecのテスト2")
    public void test_parseClassVarDec2() {
        String src = "field int x, y, z;";
        Parser p = makeParser(src);
        p.parseClassVarDec();
        String actual = p.getPrintedString();
        String expected = 
        "classVarDec\n"+
        "    'field'\n"+
        "    type\n"+
        "        'int'\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'x'\n"+
        "    ','\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'y'\n"+
        "    ','\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'z'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseTypeのテスト")
    public void test_parseType() {
        String src = "int";
        Parser p = makeParser(src);
        p.parseType();
        String actual = p.getPrintedString();
        String expected = 
        "type\n"+
        "    'int'\n";
        assertEquals(expected, actual, src);

        src = "char";
        p = makeParser(src);
        p.parseType();
        actual = p.getPrintedString();
        expected = 
        "type\n"+
        "    'char'\n";
        assertEquals(expected, actual, src);

        src = "boolean";
        p = makeParser(src);
        p.parseType();
        actual = p.getPrintedString();
        expected = 
        "type\n"+
        "    'boolean'\n";
        assertEquals(expected, actual, src);

        src = "Main";
        p = makeParser(src);
        p.parseType();
        actual = p.getPrintedString();
        expected = 
        "type\n"+
        "    className\n"+
        "        identifier\n"+
        "            'Main'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseSubroutineDecのテスト")
    public void test_parseSubroutineDec() {
        String src = "function void main() { }";
        Parser p = makeParser(src);
        p.parseSubroutineDec();
        String actual = p.getPrintedString();
        String expected = 
        "subroutineDec\n"+
        "    'function'\n"+
        "    'void'\n"+
        "    subroutineName\n"+
        "        identifier\n"+
        "            'main'\n"+
        "    '('\n"+
        "    parameterList\n"+
        "    ')'\n"+
        "    subroutineBody\n"+
        "        '{'\n"+
        "        statements\n"+
        "        '}'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseSubroutineDecのテスト2")
    public void test_parseSubroutineDec2() {
        String src = "method int f(int a, int b) { var int x; }";
        Parser p = makeParser(src);
        p.parseSubroutineDec();
        String actual = p.getPrintedString();
        String expected =
        "subroutineDec\n"+
        "    'method'\n"+
        "    type\n"+
        "        'int'\n"+
        "    subroutineName\n"+
        "        identifier\n"+
        "            'f'\n"+
        "    '('\n"+
        "    parameterList\n"+
        "        type\n"+
        "            'int'\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'a'\n"+
        "        ','\n"+
        "        type\n"+
        "            'int'\n"+
        "        varName\n"+
        "            identifier\n"+
        "                'b'\n"+
        "    ')'\n"+
        "    subroutineBody\n"+
        "        '{'\n"+
        "        varDec\n"+
        "            'var'\n"+
        "            type\n"+
        "                'int'\n"+
        "            varName\n"+
        "                identifier\n"+
        "                    'x'\n"+
        "            ';'\n"+
        "        statements\n"+
        "        '}'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseVarDecのテスト")
    public void test_parseVarDec() {
        String src = "var int x;";
        Parser p = makeParser(src);
        p.parseVarDec();
        String actual = p.getPrintedString();
        String expected = 
        "varDec\n"+
        "    'var'\n"+
        "    type\n"+
        "        'int'\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'x'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseVarDecのテスト2")
    public void test_parseVarDec2() {
        String src = "var int x, y, z;";
        Parser p = makeParser(src);
        p.parseVarDec();
        String actual = p.getPrintedString();
        String expected = 
        "varDec\n"+
        "    'var'\n"+
        "    type\n"+
        "        'int'\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'x'\n"+
        "    ','\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'y'\n"+
        "    ','\n"+
        "    varName\n"+
        "        identifier\n"+
        "            'z'\n"+
        "    ';'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseClassNameのテスト")
    public void test_parseClassName() {
        String src = "Main";
        Parser p = makeParser(src);
        p.parseClassName();
        String actual = p.getPrintedString();
        String expected = 
        "className\n"+
        "    identifier\n"+
        "        'Main'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseSubroutineNameのテスト")
    public void test_parseSubroutineName() {
        String src = "main";
        Parser p = makeParser(src);
        p.parseSubroutineName();
        String actual = p.getPrintedString();
        String expected = 
        "subroutineName\n"+
        "    identifier\n"+
        "        'main'\n";
        assertEquals(expected, actual, src);
    }
    @Test
    @DisplayName("parseVarNameのテスト")
    public void test_parseVarName() {
        String src = "x";
        Parser p = makeParser(src);
        p.parseVarName();
        String actual = p.getPrintedString();
        String expected = 
        "varName\n"+
        "    identifier\n"+
        "        'x'\n";
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;
import java.util.*;

class FindTokenTest {
    private static Map<String,Integer> findBlanksTest = new HashMap<String,Integer>();
    static {
        //findBlanksTest.put("",0);
        findBlanksTest.put(" ",1);
        findBlanksTest.put("   ",3);
        findBlanksTest.put("abc",0);
        findBlanksTest.put("   abc",3);
        findBlanksTest.put("    123",4);
        findBlanksTest.put("     {}",5);
    }
    @Test
    @DisplayName("findBlanksのテスト")
    public void test_findBlanks() {
        for (String s: findBlanksTest.keySet()) {
            int actual = Tokenizer.findBlanks(s);
            int expected = findBlanksTest.get(s);
            String message = String.format("findBlanks(\"%s\")",s);
            assertEquals(expected, actual, message);
        }
    }
    
    private static Map<String,Integer> findIdentifierTest = new HashMap<String,Integer>();
    static {
        //findIdentifierTest.put("",-1);
        findIdentifierTest.put("a",1);
        findIdentifierTest.put("aA1_",4);
        findIdentifierTest.put("abc123",6);
        findIdentifierTest.put("_789",4);

        findIdentifierTest.put("a ",1);
        findIdentifierTest.put("aA1_ ",4);
        findIdentifierTest.put("abc123 ",6);
        findIdentifierTest.put("_789 ",4);

        findIdentifierTest.put("a)",1);
        findIdentifierTest.put("aA1_)",4);
        findIdentifierTest.put("abc123)",6);
        findIdentifierTest.put("_789)",4);

        findIdentifierTest.put(" abc",-1);
        findIdentifierTest.put("1abc",-1);
        findIdentifierTest.put("(abc",-1);
    }
    @Test
    @DisplayName("findIdentifierのテスト")
    public void test_findIdentifier() {
        for (String s: findIdentifierTest.keySet()) {
            int actual = Tokenizer.findIdentifier(s);
            int expected = findIdentifierTest.get(s);
            String message = String.format("findSymbol(\"%s\")",s);
            assertEquals(expected, actual, message);
        }
    }

    private static Map<String,Integer> findSymbolTest = new HashMap<String,Integer>();
    static {
        findSymbolTest.put(" abc",-1);
        findSymbolTest.put("abc",-1);
        String symbols = "{}()[].,;+-*/&|<>=~";
        for (int i = 0; i < symbols.length(); i++) {
            char symbol = symbols.charAt(i);
            findSymbolTest.put(symbol+" abc",1);
            findSymbolTest.put(symbol+"abc",1);    
        }
    }
    @Test
    @DisplayName("findSymbolのテスト")
    public void test_findSymbol() {
        for (String s: findSymbolTest.keySet()) {
            int actual = Tokenizer.findSymbol(s);
            int expected = findSymbolTest.get(s);
            String message = String.format("findSymbol(\"%s\")",s);
            assertEquals(expected, actual, message);
        }
    }

    private static Map<String,Integer> findIntegerConstantTest = new HashMap<String,Integer>();
    static {
        //findIntegerConstantTest.put("",-1);
        findIntegerConstantTest.put(" 123",-1);
        findIntegerConstantTest.put("{123",-1);
        findIntegerConstantTest.put("a123",-1);
        findIntegerConstantTest.put("1",1);
        findIntegerConstantTest.put("123",3);
        findIntegerConstantTest.put("1234567890",10);
        findIntegerConstantTest.put("1 ",1);
        findIntegerConstantTest.put("123 ",3);
        findIntegerConstantTest.put("1234567890 ",10);
        findIntegerConstantTest.put("1{",1);
        findIntegerConstantTest.put("123{",3);
        findIntegerConstantTest.put("1234567890{",10);
        findIntegerConstantTest.put("1a",1);
        findIntegerConstantTest.put("123a",3);
        findIntegerConstantTest.put("1234567890a",10);
    }
    @Test
    @DisplayName("findIntegerConstantのテスト")
    public void test_findIntegerConstant() {
        for (String s: findIntegerConstantTest.keySet()) {
            int actual = Tokenizer.findIntegerConstant(s);
            int expected = findIntegerConstantTest.get(s);
            String message = String.format("findSymbol(\"%s\")",s);
            assertEquals(expected, actual, message);
        }
    }

    private static Map<String,Integer> findStringConstantTest = new HashMap<String,Integer>();
    static {
        //findStringConstantTest.put("",-1);
        findStringConstantTest.put(" \"abc\"",-1);
        findStringConstantTest.put("{\"abc\"",-1);
        findStringConstantTest.put("a\"abc\"",-1);
        findStringConstantTest.put("\"\"",2);
        findStringConstantTest.put("\"a\"",3);
        findStringConstantTest.put("\"abc\"",5);
        findStringConstantTest.put("\"{ }\"",5);
        findStringConstantTest.put("\"hello, world\"",14);
        findStringConstantTest.put("\"\" ",2);
        findStringConstantTest.put("\"a\"1",3);
        findStringConstantTest.put("\"abc\"a",5);
    }
    @Test
    @DisplayName("findStringConstantのテスト")
    public void test_findStringConstant() {
        for (String s: findStringConstantTest.keySet()) {
            int actual = Tokenizer.findStringConstant(s);
            int expected = findStringConstantTest.get(s);
            String message = String.format("findSymbol(%s)",s);
            assertEquals(expected, actual, message);
        }
    }
}
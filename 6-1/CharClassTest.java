import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.*;

class CharClassTest {
    private boolean[] isBlankTable = {
    //  0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
/* 0 */ false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,
/* 1 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 2 */ true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 3 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 4 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 5 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 6 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 7 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false
    };
	@Test
	@DisplayName("isBlankのテスト")
	public void test_isBlank() {
        for (int i = 0; i < isBlankTable.length; i++) {
            char ch = (char)i;
            boolean actual = Tokenizer.isBlank(ch);
            boolean expected = isBlankTable[i];
            String message = String.format("isBlank('%c')",ch);
            assertEquals(expected, actual, message);
        }
	}
    private boolean[] isDigitTable = {
    //  0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
/* 0 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 1 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 2 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 3 */ true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,false,false,false,
/* 4 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 5 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 6 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 7 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false
    };
	@Test
	@DisplayName("isDigitのテスト")
	public void test_isDigit() {
        for (int i = 0; i < isDigitTable.length; i++) {
            char ch = (char)i;
            boolean actual = Tokenizer.isDigit(ch);
            boolean expected = isDigitTable[i];
            String message = String.format("isDigit('%c')",ch);
            assertEquals(expected, actual, message);
        }
	}
    private boolean[] isAlphabetTable = {
    //  0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
/* 0 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 1 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 2 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 3 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 4 */ false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,
/* 5 */ true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,false,true ,
/* 6 */ false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,
/* 7 */ true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,false,false
    };
	@Test
	@DisplayName("isAlphabetのテスト")
	public void test_isAlphabet() {
        for (int i = 0; i < isAlphabetTable.length; i++) {
            char ch = (char)i;
            boolean actual = Tokenizer.isAlphabet(ch);
            boolean expected = isAlphabetTable[i];
            String message = String.format("isAlphabet('%c')",ch);
            assertEquals(expected, actual, message);
        }
	}
    private boolean[] isSymbolTable = {
    //  0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
/* 0 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 1 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 2 */ false,false,false,false,false,false,true ,false,true ,true ,true ,true ,true ,true ,true ,true ,
/* 3 */ false,false,false,false,false,false,false,false,false,false,false,true ,true ,true ,true ,false,
/* 4 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 5 */ false,false,false,false,false,false,false,false,false,false,false,true ,false,true ,false,false,
/* 6 */ false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,
/* 7 */ false,false,false,false,false,false,false,false,false,false,false,true ,true ,true ,true ,false
    };
    @Test
    @DisplayName("isSymbolのテスト")
    public void test_isSymbol() {
        for (int i = 0; i < isSymbolTable.length; i++) {
            char ch = (char)i;
            boolean actual = Tokenizer.isSymbol(ch);
            boolean expected = isSymbolTable[i];
            String message = String.format("isSymbol('%c')",ch);
            assertEquals(expected, actual, message);
        }
    }       
}
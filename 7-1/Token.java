class Token {
    private String tokenString;
    private static int KEYWORD = 0;
    private static int SYMBOL = 1;
    private static int INTEGER_CONSTANT = 2;
    private static int STRING_CONSTANT = 3;
    private static int IDENTIFIER = 4;
    private String[] typeString = {"Keyword", "Symbol", "IntegerConstant", "StringConstant", "Identifier"};
    private int type;
    private Token(String t, int tt) {
        tokenString = t;
        type = tt;
    }
    public static Token newKeyword(String t) {
        return new Token(t, KEYWORD);
    }
    public static Token newSymbol(String t) {
        return new Token(t, SYMBOL);
    }
    public static Token newIntegerConstant(String t) {
        return new Token(t, INTEGER_CONSTANT);
    }
    public static Token newStringConstant(String t) {
        return new Token(t, STRING_CONSTANT);
    }
    public static Token newIdentifier(String t) {
        return new Token(t, IDENTIFIER);
    }
    public boolean isKeyword() {
        return type == KEYWORD;
    }
    public boolean isSymbol() {
        return type == SYMBOL;
    }
    public boolean isIntegerConstant() {
        return type == INTEGER_CONSTANT;
    }
    public boolean isStringConstant() {
        return type == STRING_CONSTANT;
    }
    public boolean isIdentifier() {
        return type == IDENTIFIER;
    }
    public boolean is(String s) {
        return tokenString.equals(s);
    }
    public boolean isOneOf(String... tokens) {
        for (String t: tokens) {
            if (tokenString.equals(t)) {
                return true;
            }
        }
        return false;
    }
    public String tokenString() {
        return tokenString;
    }
    public String toString() {
        return tokenString+","+typeString[type];
    }
}
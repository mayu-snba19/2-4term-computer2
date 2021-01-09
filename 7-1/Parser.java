import java.util.*;
import java.io.*;

class Parser {
    private List<Token> tokens;
    private int index;
    private PrintStream out;
    private ByteArrayOutputStream bout;

    Parser(List<Token> t) {
        this(t, false);
    }

    Parser(List<Token> t, boolean printToString) {
        tokens = t;
        index = 0;
        if (printToString) {
            bout = new ByteArrayOutputStream();
            out = new PrintStream(bout, true); // autoFlush = true
        } else {
            out = System.out;
        }
    }

    public String getPrintedString() {
        return (bout != null) ? bout.toString() : "";
    }

    Token currentToken() {
        return (index < tokens.size()) ? tokens.get(index) : null;
    }

    Token nextToken() {
        return (index < tokens.size() - 1) ? tokens.get(index + 1) : null;
    }

    void advanceToNextToken() {
        index++;
    }

    boolean currentTokenIs(String s) {
        return currentToken() != null && currentToken().is(s);
    }

    boolean currentTokenIsOneOf(String... s) {
        return currentToken() != null && currentToken().isOneOf(s);
    }

    boolean nextTokenIs(String s) {
        return nextToken() != null && nextToken().is(s);
    }

    boolean nextTokenIsOneOf(String... s) {
        return nextToken() != null && nextToken().isOneOf(s);
    }

    private int parseTreeIndentLevel = 0;

    void startNonterminal(String s) {
        printNonterminalNode(s);
        parseTreeIndentLevel++;
    }

    void endNonterminal(String s) {
        parseTreeIndentLevel--;
    }

    void printTerminalNode(String s) {
        printParseTreeNode("'" + s + "'");
    }

    void printNonterminalNode(String s) {
        printParseTreeNode(s);
    }

    void printParseTreeNode(String s) {
        indent();
        out.println(s);
    }

    void indent() {
        int nSpace = parseTreeIndentLevel * 4;
        for (int i = 0; i < nSpace; i++) {
            out.print(" ");
        }
    }

    void error() {
        System.out.println("文法エラー");
        // System.exit(1);
        /*
         * 文法エラーになるテストケースは入れていないので、error()が呼ばれることはないはず
         * しかし、プログラムのミスでerror()が呼ばれると、System.exit(1)で テストの途中で実行が終了してしまう
         * その場合は、一時的に下記のように例外を投げるように変更し、 テストが中断しないようにするとよい
         */
        throw new RuntimeException();
    }

    // 1
    // class → ‘class’ className ‘{‘
    // classVarDec*
    // subroutineDec*
    // ‘}’
    void parseClass() {
        startNonterminal("class");
        // 課題
        parseTerminal("class");
        parseClassName();
        parseTerminal("{");
        while (currentTokenIsOneOf("static", "field")) {
            parseClassVarDec();
        }
        while (currentTokenIsOneOf("constructor", "function", "method")) {
            parseSubroutineDec();
        }
        parseTerminal("}");
        endNonterminal("class");
    }

    // 2
    // classVarDec → (‘static’ | ‘field’) type varName (‘,’ varName)* ‘;’
    void parseClassVarDec() {
        startNonterminal("classVarDec");
        parseTerminal("static", "field");
        parseType();
        parseVarName();
        while (currentTokenIs(",")) {
            parseTerminal(",");
            parseVarName();
        }
        parseTerminal(";");
        endNonterminal("classVarDec");
    }

    // 3
    // type → ‘int’ | ‘char’ | ‘boolean’ | className
    boolean currentTokenIsType() {
        return currentTokenIsOneOf("int", "char", "boolean") || currentTokenIsClassName();
    }

    void parseType() {
        startNonterminal("type");
        if (currentTokenIsOneOf("int", "char", "boolean")) {
            parseTerminal("int", "char", "boolean");
        } else if (currentTokenIsClassName()) {
            parseClassName();
        }
        endNonterminal("type");
    }

    // 4
    // subroutineDec → (‘constructor’ | ‘function’ | ‘method’) (‘void’ | type)
    // subroutineName ‘(’ parameterList ‘)’
    // subroutineBody
    void parseSubroutineDec() {
        startNonterminal("subroutineDec");
        parseTerminal("constructor", "function", "method");
        if (currentTokenIs("void")) {
            parseTerminal("void");
        } else {
            parseType();
        }
        parseSubroutineName();
        parseTerminal("(");
        parseParameterList();
        parseTerminal(")");
        parseSubroutineBody();
        endNonterminal("subroutineDec");
    }

    // 5
    // parameterList → ((type varName) (‘,’ type varName)*)?
    void parseParameterList() {
        startNonterminal("parameterList");
        if (currentTokenIsType()) {
            parseType();
            parseVarName();
            while (currentTokenIs(",")) {
                parseTerminal(",");
                parseType();
                parseVarName();
            }
        }
        endNonterminal("parameterList");
    }

    // 6
    // subroutineBody → ‘{’
    // varDec*
    // statements
    // ‘}’
    void parseSubroutineBody() {
        startNonterminal("subroutineBody");
        parseTerminal("{");
        while (currentTokenIs("var")) {
            parseVarDec();
        }
        parseStatements();
        parseTerminal("}");
        endNonterminal("subroutineBody");
    }

    // 7
    // varDec → ‘var’ type varName (‘,’ varName)* ‘;’
    void parseVarDec() {
        startNonterminal("varDec");
        parseTerminal("var");
        parseType();
        parseVarName();
        while (currentTokenIs(",")) {
            parseTerminal(",");
            parseVarName();
        }
        parseTerminal(";");
        endNonterminal("varDec");
    }

    // 8
    // className → identifier
    boolean currentTokenIsClassName() {
        return currentToken() != null && currentToken().isIdentifier();
    }

    void parseClassName() {
        startNonterminal("className");
        parseIdentifier();
        endNonterminal("className");
    }

    // 9
    // subroutineName → identifier
    void parseSubroutineName() {
        startNonterminal("subroutineName");
        parseIdentifier();
        endNonterminal("subroutineName");
    }

    // 10
    // varName → identifier
    void parseVarName() {
        startNonterminal("varName");
        parseIdentifier();
        endNonterminal("varName");
    }

    // 11
    // statements → statement*
    void parseStatements() {
        startNonterminal("statements");
        while (currentTokenIsOneOf("let", "if", "while", "do", "return")) {
            parseStatement();
        }
        endNonterminal("statements");
    }

    // 12
    // statement → letStatement |
    // ifStatement |
    // whileStatement |
    // doStatement |
    // returnStatement
    void parseStatement() {
        startNonterminal("statement");
        if (currentTokenIs("let")) {
            parseLetStatement();
        } else if (currentTokenIs("if")) {
            parseIfStatement();
        } else if (currentTokenIs("while")) {
            parseWhileStatement();
        } else if (currentTokenIs("do")) {
            parseDoStatement();
        } else if (currentTokenIs("return")) {
            parseReturnStatement();
        } else {
            error();
        }
        endNonterminal("statement");
    }

    // 13
    // letStatement → ‘let’ varName (‘[’ expression ‘]’)? ‘=’ expression ‘;’
    void parseLetStatement() {
        startNonterminal("letStatement");
        // 課題
        parseTerminal("let");
        parseVarName();
        if (!currentTokenIs("=")) {
            parseTerminal("[");
            parseExpression();
            parseTerminal("]");
        }
        parseTerminal("=");
        parseExpression();
        parseTerminal(";");
        endNonterminal("letStatement");
    }

    // 14
    // ifStatement → ‘if’ ‘(’ expression ‘)’ ‘{’
    // statements
    // ‘}’ (‘else’ ‘{’
    // statements
    // ‘}’)?
    void parseIfStatement() {
        startNonterminal("ifStatement");
        // 課題
        parseTerminal("if");
        parseTerminal("(");
        parseExpression();
        parseTerminal(")");
        parseTerminal("{");
        parseStatements();
        parseTerminal("}");
        if (currentTokenIs("else")) {
            parseTerminal("else");
            parseTerminal("{");
            parseStatements();
            parseTerminal("}");
        }
        endNonterminal("ifStatement");
    }

    // 15
    // whileStatement → ‘while’ ‘(’ expression ‘)’ ‘{’
    // statements
    // ‘}’
    void parseWhileStatement() {
        startNonterminal("whileStatement");
        parseTerminal("while");
        parseTerminal("(");
        parseExpression();
        parseTerminal(")");
        parseTerminal("{");
        parseStatements();
        parseTerminal("}");
        endNonterminal("whileStatement");
    }

    // 16
    // doStatement → ‘do’ subroutineCall ‘;’
    void parseDoStatement() {
        startNonterminal("doStatement");
        parseTerminal("do");
        parseSubroutineCall();
        parseTerminal(";");
        endNonterminal("doStatement");
    }

    // 17
    // returnStatement → ‘return’ expression? ‘;’
    void parseReturnStatement() {
        startNonterminal("returnStatement");
        parseTerminal("return");
        if (!currentTokenIs(";")) {
            parseExpression();
        }
        parseTerminal(";");
        endNonterminal("returnStatement");
    }

    // 18
    // expression → term (op term)*
    void parseExpression() {
        startNonterminal("expression");
        parseTerm();
        while (currentTokenIsOp()) {
            parseOp();
            parseTerm();
        }
        endNonterminal("expression");
    }

    // 19
    // term → integerConstant |
    // stringConstant |
    // keywordConstant |
    // varName |
    // varName ‘[’ expression ‘]’ |
    // subroutineCall |
    // ‘(’ expression ‘)’ |
    // unaryOp term
    void parseTerm() {
        startNonterminal("term");
        if (currentTokenIsIntegerConstant()) {
            parseIntegerConstant();
        } else if (currentTokenIsStringConstant()) {
            parseStringConstant();
        } else if (currentTokenIsKeywordConstant()) {
            parseKeywordConstant();
        } else if (currentTokenIs("(")) {
            parseTerminal("(");
            parseExpression();
            parseTerminal(")");
        } else if (currentTokenIsUnaryOp()) {
            parseUnaryOp();
            parseTerm();
        } else if (nextTokenIsOneOf("(", ".")) {
            parseSubroutineCall();
        } else if (nextTokenIs("[")) {
            parseVarName();
            parseTerminal("[");
            parseExpression();
            parseTerminal("]");
        } else {
            parseVarName();
        }
        endNonterminal("term");
    }

    // 20
    // subroutineCall → subroutineName ‘(’ expressionList ‘)’ |
    // (className | varName) ‘.’ subroutineName ‘(’ expressionList ‘)’
    void parseSubroutineCall() {
        startNonterminal("subroutineCall");
        if (nextTokenIs("(")) {
            parseSubroutineName();
            parseTerminal("(");
            parseExpressionList();
            parseTerminal(")");
        } else if (nextTokenIs(".")) {
            parseVarName(); // or className
            parseTerminal(".");
            parseSubroutineName();
            parseTerminal("(");
            parseExpressionList();
            parseTerminal(")");
        } else {
            error();
        }
        endNonterminal("subroutineCall");
    }

    // 21
    // expressionList → (expression (‘,’ expression) * )?
    void parseExpressionList() {
        startNonterminal("expressionList");
        if (currentTokenIs(")")) {
        } else {
            parseExpression();
            while (currentTokenIs(",")) {
                parseTerminal(",");
                parseExpression();
            }
        }
        endNonterminal("expressionList");
    }

    // 22
    // op → ‘+’|‘-’|‘*’|‘/’|‘&’|‘|’|‘<’|‘>’|‘=’
    boolean currentTokenIsOp() {
        return currentTokenIsOneOf("+", "-", "*", "/", "&", "|", "<", ">", "=");
    }

    void parseOp() {
        startNonterminal("op");
        if (currentTokenIsOp()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("op");
    }

    // 23
    // unaryOp → ‘-’ | ‘~’
    boolean currentTokenIsUnaryOp() {
        return currentTokenIsOneOf("-", "~");
    }

    void parseUnaryOp() {
        startNonterminal("unaryOp");
        if (currentTokenIsUnaryOp()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("unaryOp");
    }

    // 24
    // keywordConstant → ‘true’ | ‘false’ | ‘null’ | ‘this’
    boolean currentTokenIsKeywordConstant() {
        return currentTokenIsOneOf("true", "false", "null", "this");
    }

    void parseKeywordConstant() {
        startNonterminal("keywordConstant");
        if (currentTokenIsKeywordConstant()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("keyowrdConstant");
    }

    // token
    boolean currentTokenIsIntegerConstant() {
        return currentToken() != null && currentToken().isIntegerConstant();
    }

    void parseIntegerConstant() {
        startNonterminal("integerConstant");
        if (currentTokenIsIntegerConstant()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("integerConstant");
    }

    boolean currentTokenIsStringConstant() {
        return currentToken() != null && currentToken().isStringConstant();
    }

    void parseStringConstant() {
        startNonterminal("stringConstant");
        if (currentTokenIsStringConstant()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("stringConstant");
    }

    boolean currentTokenIsIdentifier() {
        return currentToken() != null && currentToken().isIdentifier();
    }

    void parseIdentifier() {
        startNonterminal("identifier");
        if (currentTokenIsIdentifier()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("identifier");
    }

    // symbol or keyword
    void parseTerminal(String... tokens) {
        if (currentToken().isOneOf(tokens)) {
            parseTerminal();
        } else {
            error();
        }
    }

    void parseTerminal() {
        printTerminalNode(currentToken().tokenString());
        advanceToNextToken();
    }

    static List<Token> readTokensFromStdin() {
        Scanner in = new Scanner(System.in);
        Tokenizer t = new Tokenizer();
        List<Token> tokens = new ArrayList<Token>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            tokens.addAll(t.tokenize(line));
        }
        return tokens;
    }

    public static void main(String[] args) {
        List<Token> tokens = readTokensFromStdin();
        Parser p = new Parser(tokens);
        p.parseClass();
    }
}

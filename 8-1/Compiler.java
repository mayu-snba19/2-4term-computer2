import java.util.*;
import java.io.*;

class Compiler {
    private List<Token> tokens;
    private int index;
    private PrintStream out;
    private ByteArrayOutputStream bout;
    private CodeGenerator codeGenerator;

    Compiler(List<Token> t) {
        this(t, false);
    }

    Compiler(List<Token> t, boolean printToString) {
        tokens = t;
        index = 0;
        codeGenerator = new CodeGenerator();
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
        System.err.println("文法エラー");
        System.exit(1);
    }

    // 1
    // class → ‘class’ className ‘{‘
    // classVarDec*
    // subroutineDec*
    // ‘}’
    VmCode compileClass() {
        startNonterminal("class");
        parseTerminal("class");
        String className = parseClassName();
        codeGenerator.startClass(className);
        VmCode code = new VmCode();
        parseTerminal("{");
        while (currentTokenIsOneOf("static", "field")) {
            parseClassVarDec();
        }
        while (currentTokenIsOneOf("constructor", "function", "method")) {
            VmCode subroutineCode = compileSubroutineDec();
            code.addVmCode(subroutineCode);
        }
        parseTerminal("}");
        endNonterminal("class");
        return code;
    }

    // 2
    // classVarDec → (‘static’ | ‘field’) type varName (‘,’ varName)* ‘;’
    void parseClassVarDec() {
        startNonterminal("classVarDec");
        String kind = parseTerminal("static", "field");
        String type = parseType();
        String name = parseVarName();
        codeGenerator.defineVariable(kind, name, type);
        while (currentTokenIs(",")) {
            parseTerminal(",");
            name = parseVarName();
            codeGenerator.defineVariable(kind, name, type);
        }
        parseTerminal(";");
        endNonterminal("classVarDec");
    }

    // 3
    // type → ‘int’ | ‘char’ | ‘boolean’ | className
    boolean currentTokenIsType() {
        return currentTokenIsOneOf("int", "char", "boolean") || currentTokenIsClassName();
    }

    String parseType() {
        startNonterminal("type");
        String typeString = null;
        if (currentTokenIsOneOf("int", "char", "boolean")) {
            typeString = parseTerminal("int", "char", "boolean");
        } else if (currentTokenIsClassName()) {
            typeString = parseClassName();
        }
        endNonterminal("type");
        return typeString;
    }

    // 4
    // subroutineDec → (‘constructor’ | ‘function’ | ‘method’) (‘void’ | type)
    // subroutineName ‘(’ parameterList ‘)’
    // subroutineBody
    VmCode compileSubroutineDec() {
        startNonterminal("subroutineDec");
        String kind = parseTerminal("constructor", "function", "method");
        if (currentTokenIs("void")) {
            parseTerminal("void");
        } else {
            parseType();
        }
        String name = parseSubroutineName();
        codeGenerator.startSubroutine(kind);
        parseTerminal("(");
        parseParameterList();
        parseTerminal(")");
        VmCode body = compileSubroutineBody();
        // ローカル変数の個数が必要なのでbodyをコンパイルした後でheadのコードを生成
        VmCode head = codeGenerator.subroutineHead(name);
        endNonterminal("subroutineDec");
        return codeGenerator.subroutineDec(head, body);
    }

    // 5
    // parameterList → ((type varName) (‘,’ type varName)*)?
    void parseParameterList() {
        startNonterminal("parameterList");
        if (currentTokenIsType()) {
            String kind = "parameter";
            String type = parseType();
            String name = parseVarName();
            codeGenerator.defineVariable(kind, name, type);
            while (currentTokenIs(",")) {
                parseTerminal(",");
                type = parseType();
                name = parseVarName();
                codeGenerator.defineVariable(kind, name, type);
            }
        }
        endNonterminal("parameterList");
    }

    // 6
    // subroutineBody → ‘{’
    // varDec*
    // statements
    // ‘}’
    VmCode compileSubroutineBody() {
        startNonterminal("subroutineBody");
        parseTerminal("{");
        while (currentTokenIs("var")) {
            parseVarDec();
        }
        VmCode code = compileStatements();
        parseTerminal("}");
        endNonterminal("subroutineBody");
        return code;
    }

    // 7
    // varDec → ‘var’ type varName (‘,’ varName)* ‘;’
    void parseVarDec() {
        startNonterminal("varDec");
        parseTerminal("var");
        String kind = "var";
        String type = parseType();
        String name = parseVarName();
        codeGenerator.defineVariable(kind, name, type);
        while (currentTokenIs(",")) {
            parseTerminal(",");
            name = parseVarName();
            codeGenerator.defineVariable(kind, name, type);
        }
        parseTerminal(";");
        endNonterminal("varDec");
    }

    // 8
    // className → identifier
    boolean currentTokenIsClassName() {
        return currentToken() != null && currentToken().isIdentifier();
    }

    String parseClassName() {
        startNonterminal("className");
        String className = parseIdentifier();
        endNonterminal("className");
        return className;
    }

    // 9
    // subroutineName → identifier
    String parseSubroutineName() {
        startNonterminal("subroutineName");
        String subroutineName = parseIdentifier();
        endNonterminal("subroutineName");
        return subroutineName;
    }

    // 10
    // varName → identifier
    String parseVarName() {
        startNonterminal("varName");
        String varName = parseIdentifier();
        endNonterminal("varName");
        return varName;
    }

    // 11
    // statements → statement*
    VmCode compileStatements() {
        startNonterminal("statements");
        VmCode code = new VmCode();
        while (currentTokenIsOneOf("let", "if", "while", "do", "return")) {
            VmCode statementCode = compileStatement();
            code.addVmCode(statementCode);
        }
        endNonterminal("statements");
        return code;
    }

    // 12
    // statement → letStatement |
    // ifStatement |
    // whileStatement |
    // doStatement |
    // returnStatement
    VmCode compileStatement() {
        startNonterminal("statement");
        VmCode code = null;
        if (currentTokenIs("let")) {
            code = compileLetStatement();
        } else if (currentTokenIs("if")) {
            code = compileIfStatement();
        } else if (currentTokenIs("while")) {
            code = compileWhileStatement();
        } else if (currentTokenIs("do")) {
            code = compileDoStatement();
        } else if (currentTokenIs("return")) {
            code = compileReturnStatement();
        } else {
            error();
        }
        endNonterminal("statement");
        return code;
    }

    // 13
    // letStatement → ‘let’ varName (‘[’ expression ‘]’)? ‘=’ expression ‘;’
    VmCode compileLetStatement() {
        startNonterminal("letStatement");
        parseTerminal("let");
        String name = parseVarName();
        VmCode indexCode = null;
        if (currentTokenIs("[")) {
            parseTerminal("[");
            indexCode = compileExpression();
            parseTerminal("]");
        }
        parseTerminal("=");
        VmCode exprCode = compileExpression();
        parseTerminal(";");
        endNonterminal("letStatement");
        return codeGenerator.letStatement(name, indexCode, exprCode);
    }

    // 14
    // ifStatement → ‘if’ ‘(’ expression ‘)’ ‘{’
    // statements
    // ‘}’ (‘else’ ‘{’
    // statements
    // ‘}’)?
    VmCode compileIfStatement() {
        startNonterminal("ifStatement");
        parseTerminal("if");
        parseTerminal("(");
        VmCode exprCode = compileExpression();
        parseTerminal(")");
        parseTerminal("{");
        VmCode thenCode = compileStatements();
        parseTerminal("}");
        VmCode elseCode = null;
        if (currentTokenIs("else")) {
            parseTerminal("else");
            parseTerminal("{");
            elseCode = compileStatements();
            parseTerminal("}");
        }
        endNonterminal("ifStatement");
        return codeGenerator.ifStatement(exprCode, thenCode, elseCode);
    }

    // 15
    // whileStatement → ‘while’ ‘(’ expression ‘)’ ‘{’
    // statements
    // ‘}’
    VmCode compileWhileStatement() {
        // 課題
        // とりあえず空のVmCodeをreturnしているが、
        // 実際にはcodeGeneratorのwhileStatementメソッドで生成したコードを返す
        startNonterminal("whileStatement");
        parseTerminal("while");
        parseTerminal("(");
        VmCode exprCode = compileExpression();
        parseTerminal(")");
        parseTerminal("{");
        VmCode statements = compileStatements();
        parseTerminal("}");
        endNonterminal("whileStatement");
        return codeGenerator.whileStatement(exprCode, statements);
    }

    // 16
    // doStatement → ‘do’ subroutineCall ‘;’
    VmCode compileDoStatement() {
        startNonterminal("doStatement");
        parseTerminal("do");
        VmCode subroutineCall = compileSubroutineCall();
        parseTerminal(";");
        endNonterminal("doStatement");
        return codeGenerator.doStatement(subroutineCall);
    }

    // 17
    // returnStatement → ‘return’ expression? ‘;’
    VmCode compileReturnStatement() {
        startNonterminal("returnStatement");
        parseTerminal("return");
        VmCode expr = null;
        if (!currentTokenIs(";")) {
            expr = compileExpression();
        }
        parseTerminal(";");
        endNonterminal("returnStatement");
        return codeGenerator.returnStatement(expr);
    }

    // 18
    // expression → term (op term)*
    VmCode compileExpression() {
        startNonterminal("expression");
        VmCode code = new VmCode();
        VmCode termCode = compileTerm();
        code.addVmCode(termCode);
        while (currentTokenIsOp()) {
            VmCode opCode = compileOp();
            termCode = compileTerm();
            code.addVmCode(termCode);
            code.addVmCode(opCode);
        }
        endNonterminal("expression");
        return code;
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
    VmCode compileTerm() {
        startNonterminal("term");
        VmCode code;
        if (currentTokenIsIntegerConstant()) {
            code = compileIntegerConstant();
        } else if (currentTokenIsStringConstant()) {
            code = compileStringConstant();
        } else if (currentTokenIsKeywordConstant()) {
            code = compileKeywordConstant();
        } else if (currentTokenIs("(")) {
            parseTerminal("(");
            code = compileExpression();
            parseTerminal(")");
        } else if (currentTokenIsUnaryOp()) {
            String op = parseUnaryOp();
            VmCode termCode = compileTerm();
            code = codeGenerator.unaryOp(op, termCode);
        } else if (nextTokenIsOneOf("(", ".")) {
            code = compileSubroutineCall();
        } else if (nextTokenIs("[")) {
            String name = parseVarName();
            parseTerminal("[");
            VmCode indexCode = compileExpression();
            parseTerminal("]");
            code = codeGenerator.pushArrayElement(name, indexCode);
        } else {
            String name = parseVarName();
            code = codeGenerator.pushVariable(name);
        }
        endNonterminal("term");
        return code;
    }

    // 20
    // subroutineCall → subroutineName ‘(’ expressionList ‘)’ |
    // (className | varName) ‘.’ subroutineName ‘(’ expressionList ‘)’
    VmCode compileSubroutineCall() {
        startNonterminal("subroutineCall");
        String classOrVarName = null;
        String subroutineName = null;
        List<VmCode> exprList = null;
        if (nextTokenIs("(")) {
            subroutineName = parseSubroutineName();
            parseTerminal("(");
            exprList = compileExpressionList();
            parseTerminal(")");
        } else if (nextTokenIs(".")) {
            classOrVarName = parseVarName(); // or className
            parseTerminal(".");
            subroutineName = parseSubroutineName();
            parseTerminal("(");
            exprList = compileExpressionList();
            parseTerminal(")");
        } else {
            error();
        }
        endNonterminal("subroutineCall");
        return codeGenerator.subroutineCall(classOrVarName, subroutineName, exprList);
    }

    // 21
    // expressionList → (expression (‘,’ expression) * )?
    List<VmCode> compileExpressionList() {
        startNonterminal("expressionList");
        List<VmCode> codeList = new ArrayList<VmCode>();
        if (currentTokenIs(")")) {
        } else {
            VmCode exprCode = compileExpression();
            codeList.add(exprCode);
            while (currentTokenIs(",")) {
                parseTerminal(",");
                exprCode = compileExpression();
                codeList.add(exprCode);
            }
        }
        endNonterminal("expressionList");
        return codeList;
    }

    // 22
    // op → ‘+’|‘-’|‘*’|‘/’|‘&’|‘|’|‘<’|‘>’|‘=’
    boolean currentTokenIsOp() {
        return currentTokenIsOneOf("+", "-", "*", "/", "&", "|", "<", ">", "=");
    }

    VmCode compileOp() {
        startNonterminal("op");
        VmCode code = null;
        if (currentTokenIsOp()) {
            String op = parseTerminal();
            code = codeGenerator.op(op);
        } else {
            error();
        }
        endNonterminal("op");
        return code;
    }

    // 23
    // unaryOp → ‘-’ | ‘~’
    boolean currentTokenIsUnaryOp() {
        return currentTokenIsOneOf("-", "~");
    }

    String parseUnaryOp() {
        startNonterminal("unaryOp");
        String unaryOp = null;
        if (currentTokenIsUnaryOp()) {
            unaryOp = parseTerminal();
        } else {
            error();
        }
        endNonterminal("unaryOp");
        return unaryOp;
    }

    // 24
    // keywordConstant → ‘true’ | ‘false’ | ‘null’ | ‘this’
    boolean currentTokenIsKeywordConstant() {
        return currentTokenIsOneOf("true", "false", "null", "this");
    }

    VmCode compileKeywordConstant() {
        startNonterminal("keywordConstant");
        VmCode code = null;
        if (currentTokenIsKeywordConstant()) {
            String keywordConst = parseTerminal();
            code = codeGenerator.keywordConstant(keywordConst);
        } else {
            error();
        }
        endNonterminal("keyowrdConstant");
        return code;
    }

    // token
    boolean currentTokenIsIntegerConstant() {
        return currentToken() != null && currentToken().isIntegerConstant();
    }

    VmCode compileIntegerConstant() {
        startNonterminal("integerConstant");
        VmCode code = null;
        if (currentTokenIsIntegerConstant()) {
            String integerConstant = parseTerminal();
            code = codeGenerator.integerConstant(integerConstant);
        } else {
            error();
        }
        endNonterminal("integerConstant");
        return code;
    }

    boolean currentTokenIsStringConstant() {
        return currentToken() != null && currentToken().isStringConstant();
    }

    VmCode compileStringConstant() {
        startNonterminal("stringConstant");
        VmCode code = null;
        if (currentTokenIsStringConstant()) {
            String stringConstant = parseTerminal();
            code = codeGenerator.stringConstant(stringConstant);
        } else {
            error();
        }
        endNonterminal("stringConstant");
        return code;
    }

    boolean currentTokenIsIdentifier() {
        return currentToken() != null && currentToken().isIdentifier();
    }

    String parseIdentifier() {
        startNonterminal("identifier");
        String tokenString = null;
        if (currentTokenIsIdentifier()) {
            tokenString = parseTerminal();
        } else {
            error();
        }
        endNonterminal("identifier");
        return tokenString;
    }

    // symbol or keyword
    String parseTerminal(String... tokens) {
        String tokenString = null;
        if (currentToken().isOneOf(tokens)) {
            tokenString = parseTerminal();
        } else {
            error();
        }
        return tokenString;
    }

    String parseTerminal() {
        String tokenString = currentToken().tokenString();
        printTerminalNode(tokenString);
        advanceToNextToken();
        return tokenString;
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
        Compiler c = new Compiler(tokens, true);
        VmCode code = c.compileClass();
        System.out.println(code);
    }
}

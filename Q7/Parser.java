import java.util.*;

class Parser {
    private String[] tokens;
    private int index;

    Parser(String s) {
        tokens = tokenize(s);
        index = 0;
    }

    String[] tokenize(String s) {
        return s.replaceAll("\\s", "").split("");
    }

    String currentToken() {
        return index < tokens.length ? tokens[index] : null;
    }

    void advanceToNextToken() {
        index++;
    }

    boolean currentTokenIs(String s) {
        return s.equals(currentToken());
        // sとしてnullは渡さないと仮定。従ってnullチェックは不要。
        // currentToken()はnullを返す可能性がある。nullチェックが必要。
        // return currentToken() != null && currentToken().equals(s);
    }

    private int parseTreeIndentLevel = 0;

    void startNonterminal(String s) {
        printNonterminalNode(s);
        parseTreeIndentLevel++;
    }

    void endNonterminal(String s) {
        parseTreeIndentLevel--;
    }

    void printTerminalNode(String token) {
        printParseTreeNode("'" + token + "'");
    }

    void printNonterminalNode(String s) {
        printParseTreeNode(s);
    }

    void printParseTreeNode(String s) {
        indent();
        System.out.println(s);
    }

    void indent() {
        int nSpace = parseTreeIndentLevel * 4;
        for (int i = 0; i < nSpace; i++) {
            System.out.print(" ");
        }
    }

    void error() {
        System.out.println("文法エラー");
        System.exit(1);
    }

    // expr → term (op term)*
    void parseExpr() {
        startNonterminal("expr");
        parseTerm();
        // 問題
        while (currentTokenIsInt() || currentTokenIsVar() || currentTokenIs("(")) {
            parseOp();
            parseTerm();
        }
        endNonterminal("expr");
    }

    // term → int |
    // var |
    // ‘(’ expr ‘)’ |
    void parseTerm() {
        startNonterminal("term");
        // 問題
        if (currentTokenIsInt()) {
            parseInt();
        } else if (currentTokenIsVar()) {
            parseVar();
        } else if (currentTokenIs("(")) {
            parseTerminal("(");
            parseExpr();
            parseTerminal(")");
        } else {
            error();
        }

        endNonterminal("term");
    }

    // op → ‘+’|‘-’
    boolean currentTokenIsOp() {
        return currentTokenIs("+") || currentTokenIs("-");
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

    // int → ‘1’|‘2’|'3'
    boolean currentTokenIsInt() {
        return currentTokenIs("1") || currentTokenIs("2") || currentTokenIs("3");
    }

    void parseInt() {
        startNonterminal("int");
        if (currentTokenIsInt()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("int");
    }

    // var → ‘x’|‘y’|'z'
    boolean currentTokenIsVar() {
        return currentTokenIs("x") || currentTokenIs("y") || currentTokenIs("z");
    }

    void parseVar() {
        startNonterminal("var");
        if (currentTokenIsVar()) {
            parseTerminal();
        } else {
            error();
        }
        endNonterminal("var");
    }

    void parseTerminal(String token) {
        if (currentTokenIs(token)) {
            parseTerminal();
        } else {
            error();
        }
    }

    void parseTerminal() {
        printTerminalNode(currentToken());
        advanceToNextToken();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String tokens = in.nextLine();
            Parser p = new Parser(tokens);
            p.parseExpr();
        }
    }
}

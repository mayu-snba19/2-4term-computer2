import java.util.*;

class Tokenizer {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Tokenizer t = new Tokenizer();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            t.printToken(line);
        }
    }
    public List<Token> tokenize(String line) {
        List<Token> tokens = new ArrayList<Token>();
        String l = line;
        while (l.length() > 0) {
            int endIndex = findBlanks(l);
            if (endIndex >= l.length()) return tokens;
            l = l.substring(endIndex);
            if (l.startsWith("//")) return tokens;
    
            if ((endIndex = findIdentifier(l)) >= 0) {
                String token = l.substring(0,endIndex);
                if (isKeyword(token)) {
                    tokens.add(Token.newKeyword(token));
                } else {
                    tokens.add(Token.newIdentifier(token));
                }
            } else if ((endIndex = findIntegerConstant(l)) >= 0) {
                String token = l.substring(0,endIndex);
                tokens.add(Token.newIntegerConstant(token));
            } else if ((endIndex = findStringConstant(l)) >= 0) {
                String token = l.substring(0,endIndex);
                tokens.add(Token.newStringConstant(token));
            } else if ((endIndex = findSymbol(l)) >= 0) {
                String token = l.substring(0,endIndex);
                tokens.add(Token.newSymbol(token));
            } else {
                endIndex = 1;
            }
            l = l.substring(endIndex);
        }
        return tokens;
    }
    /*
    public void tokenize(String line, List<Token> tokens) {
        String l = line;
        while (l.length() > 0) {
            int endIndex = findBlanks(l);
            if (endIndex >= l.length()) return;
            l = l.substring(endIndex);
            if (l.startsWith("//")) return;
    
            if ((endIndex = findIdentifier(l)) >= 0) {
                String token = l.substring(0,endIndex);
                if (isKeyword(token)) {
                    tokens.add(Token.newKeyword(token));
                } else {
                    tokens.add(Token.newIdentifier(token));
                }
            } else if ((endIndex = findIntegerConstant(l)) >= 0) {
                String token = l.substring(0,endIndex);
                tokens.add(Token.newIntegerConstant(token));
            } else if ((endIndex = findStringConstant(l)) >= 0) {
                String token = l.substring(0,endIndex);
                tokens.add(Token.newStringConstant(token));
            } else if ((endIndex = findSymbol(l)) >= 0) {
                String token = l.substring(0,endIndex);
                tokens.add(Token.newSymbol(token));
            } else {
                endIndex = 1;
            }
            l = l.substring(endIndex);
        }
    }
    */
    public void printToken(String line) {
        String l = line;
        while (l.length() > 0) {
            int endIndex = findBlanks(l);
            if (endIndex >= l.length()) return;
            l = l.substring(endIndex);
            if (l.startsWith("//")) return;
    
            String tokenType = "";
            String token = "";
            if ((endIndex = findIdentifier(l)) >= 0) {
                token = l.substring(0,endIndex);
                tokenType = isKeyword(token)? "keyword" : "identifier";
            } else if ((endIndex = findIntegerConstant(l)) >= 0) {
                tokenType = "integerConstant";
            } else if ((endIndex = findStringConstant(l)) >= 0) {
                tokenType = "stringConstant";
            } else if ((endIndex = findSymbol(l)) >= 0) {
                tokenType = "symbol";
            } else {
                tokenType = "?";
                endIndex = 1;
            }
            token = l.substring(0,endIndex);
            l = l.substring(endIndex);
            System.out.println(token+","+tokenType);
        }
    }

    static int findBlanks(String s) {
        /*
          文字列sは空文字列ではないとする
          文字列sの先頭から0文字以上の空白文字の列を見つける
          0文字以上なので常に存在する
          0文字以上の空白文字の列の終端のindexを返す
          s.substring(0,index)が0文字以上の空白文字の列になる
         */
        int index = 0;
        while (index < s.length() && isBlank(s.charAt(index))) {
                index++;
        }
        return index;
    }

    static int findIdentifier(String s) {
        /*
          文字列sは空文字列ではないとする
          文字列sの先頭からidentifierを見つける
          identifierは1文字以上の英字で始まる英数字の列
          見つかった場合
          　identifierの終端のindexを返す
          　s.substring(0,index)がidentifierになる
          見つからない場合
          　-1を返す
         */
        //return -1;

        if (!isAlphabet(s.charAt(0))) {
            return -1;
        }
        int index = 1;
        while (index < s.length() && isAlphabetOrDigit(s.charAt(index))) {
            index++;
        }
        return index;

    }

    static int findSymbol(String s) {
        /*
          文字列sは空文字列ではないとする
          文字列sの先頭からsymbolを見つける
          symbolは1文字の特定の記号
          見つかった場合
          　symbolの終端のindexを返す
          　s.substring(0,index)がsymbolになる
          　symbolは1文字なので、indexは常に1
          見つからない場合
          　-1を返す
         */
        // return -1;

        //if (s.length() == 0) return -1;
        if (!isSymbol(s.charAt(0))) {
            return -1;
        }
        return 1;

    }

    static int findIntegerConstant(String s) {
        /*
          文字列sは空文字列ではないとする
          文字列sの先頭からintegerConstantを見つける
          integerConstantは1文字以上数字の列
          見つかった場合
          　integerConstantの終端のindexを返す
          　s.substring(0,index)がintegerConstantになる
          見つからない場合
          　-1を返す
         */
        //return -1;

        //if (s.length() == 0) return -1;
        if (!isDigit(s.charAt(0))) {
            return -1;
        }
        int index = 1;
        while (index < s.length() && isDigit(s.charAt(index))) {
            index++;
        }
        return index;
    }

    static int findStringConstant(String s) {
        /*
          文字列sは空文字列ではないとする
          文字列sの先頭からstringConstantを見つける
          stringConstantは二重引用符（"）で囲まれた文字列
          中の文字列には二重引用符（および改行）を含まない
          見つかった場合
          　stringConstantの終端のindexを返す
          　s.substring(0,index)がstringConstant（両端の""を含む）になる
          見つからない場合
          　-1を返す
         */
        //return -1;

        //if (s.length() == 0) return -1;
        if (s.charAt(0) != '"') {
            return -1;
        }
        int index = 1;
        while (index < s.length() && s.charAt(index) != '"') {
            index++;
        }
        return index + 1;
    }

    static final int B = 1; // blank
    static final int A = 2; // alphabet
    static final int D = 3; // digit
    static final int S = 4; // symbol
    static int charClassTable[] = {
    //  0 1 2 3 4 5 6 7 8 9 a b c d e f
        0,0,0,0,0,0,0,0,0,B,0,0,0,0,0,0, // 0
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, // 1
        B,0,0,0,0,0,S,0,S,S,S,S,S,S,S,S, // 2
        D,D,D,D,D,D,D,D,D,D,0,S,S,S,S,0, // 3
        0,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A, // 4
        A,A,A,A,A,A,A,A,A,A,A,S,0,S,0,A, // 5
        0,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A, // 6
        A,A,A,A,A,A,A,A,A,A,A,S,S,S,S,0, // 7
    };

    static boolean isBlank(char c) {
        /*
        cはASCIIコードの範囲の文字（文字コードが0から127）とする
        cが空白文字（スペースまたはタブ）かどうかを返す
        */
//        return c == ' ' || c == '\t';
        return charClassTable[c] == B;
    }
    static boolean isAlphabet(char c) {
        /*
        cはASCIIコードの範囲の文字（文字コードが0から127）とする
        cがアルファベット（大文字の'A'から'Z'、小文字の'a'から'z'、'_'）かどうかを返す
        */
//        return ('A' <= c && c <= 'Z')
//            || ('a' <= c && c <= 'z')
//            || c == '_';
        return charClassTable[c] == A;
    }
    static boolean isDigit(char c) {
        /*
        cはASCIIコードの範囲の文字（文字コードが0から127）とする
        cが数字（'0'から'9'）かどうかを返す
        */
//        return ('0' <= c && c <= '9');
        return charClassTable[c] == D;
    }
    static boolean isAlphabetOrDigit(char c) {
        /*
        cはASCIIコードの範囲の文字（文字コードが0から127）とする
        cがアルファベットまたは数字かどうかを返す
        */
        return isAlphabet(c) || isDigit(c);
    }
    static boolean isSymbol(char c) {
        /*
        cはASCIIコードの範囲の文字（文字コードが0から127）とする
        cがJackのシンボルかどうかを返す
        Jackのシンボルは常に1文字で、下記のいずれかの文字
        {}()[].,;+-/*&|<>=~
        */
//        return "{}()[].,;+-*/&|<>=~".indexOf(c) >= 0;
        return charClassTable[c] == S;
    }

    static boolean isKeyword(String s) {
        /*
        文字列sがJackのキーワード（予約語）かどうかを返す
        */
        return keywordSet.contains(s);
    }
    private static Set<String> keywordSet = createKeywordSet();
    private static Set<String> createKeywordSet() {
        Set<String> set = new HashSet<String>();
        Collections.addAll(set,
                           "class","method","function","constructor",
                           "int","boolean","char","void",
                           "var","static","field",
                           "let","do","if","else","while","return",
                           "true","false","null","this");
        return set;
    }
}
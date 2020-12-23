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

    public void printToken(String line) {
        String l = line;
        while (l.length() > 0) {
            int endIndex = findBlanks(l);
            if (endIndex >= l.length())
                return;
            l = l.substring(endIndex);
            if (l.startsWith("//"))
                return;

            String tokenType = "";
            String token = "";
            if ((endIndex = findIdentifier(l)) >= 0) {
                token = l.substring(0, endIndex);
                tokenType = isKeyword(token) ? "??" : "??";
            } else if ((endIndex = findIntegerConstant(l)) >= 0) {
                tokenType = "??";
            } else if ((endIndex = findStringConstant(l)) >= 0) {
                tokenType = "??";
            } else if ((endIndex = findSymbol(l)) >= 0) {
                tokenType = "??";
            } else {
                tokenType = "?";
                endIndex = 1;
            }
            token = l.substring(0, endIndex);
            l = l.substring(endIndex);
            System.out.println(token + "," + tokenType);
        }
    }

    static int findBlanks(String s) {
        /*
         * 文字列sは空文字列ではないとする 文字列sの先頭から0文字以上の空白文字の列を見つける 0文字以上なので常に存在する
         * 0文字以上の空白文字の列の終端のindexを返す s.substring(0,index)が0文字以上の空白文字の列になる
         */
        int index = 0;
        while (index < s.length() && isBlank(s.charAt(index))) {
            index++;
        }
        return index;
    }

    static int findIdentifier(String s) {
        /*
         * 文字列sは空文字列ではないとする 文字列sの先頭からidentifierを見つける identifierは1文字以上の英字で始まる英数字の列
         * 見つかった場合 identifierの終端のindexを返す s.substring(0,index)がidentifierになる 見つからない場合
         * -1を返す
         */
        if (!isAlphabet(s.charAt(0)))
            return -1;
        if (s.length() == 1)
            return 1;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!isAlphabetOrDigit(c))
                return i;
            if (i == s.length() - 1)
                return i + 1;
        }

        return -1;
    }

    static int findSymbol(String s) {
        /*
         * 文字列sは空文字列ではないとする 文字列sの先頭からsymbolを見つける symbolは1文字の特定の記号 見つかった場合
         * symbolの終端のindexを返す s.substring(0,index)がsymbolになる symbolは1文字なので、indexは常に1
         * 見つからない場合 -1を返す
         */
        if (!isSymbol(s.charAt(0)))
            return -1;
        if (s.length() == 1)
            return 1;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!isSymbol(c))
                return i;
            if (i == s.length() - 1)
                return i + 1;
        }

        return -1;
    }

    static int findIntegerConstant(String s) {
        /*
         * 文字列sは空文字列ではないとする 文字列sの先頭からintegerConstantを見つける integerConstantは1文字以上数字の列
         * 見つかった場合 integerConstantの終端のindexを返す s.substring(0,index)がintegerConstantになる
         * 見つからない場合 -1を返す
         */
        if (!isDigit(s.charAt(0)))
            return -1;
        if (s.length() == 1)
            return 1;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!isDigit(c))
                return i;
            if (i == s.length() - 1)
                return i + 1;
        }
        return -1;
    }

    static int findStringConstant(String s) {
        /*
         * 文字列sは空文字列ではないとする 文字列sの先頭からstringConstantを見つける stringConstantは二重引用符（"）で囲まれた文字列
         * 中の文字列には二重引用符（および改行）を含まない 見つかった場合 stringConstantの終端のindexを返す
         * s.substring(0,index)がstringConstant（両端の""を含む）になる 見つからない場合 -1を返す
         */
        if (s.charAt(0) != '"')
            return -1;
        if (s.length() == 2)
            return 2;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"')
                return i + 1;
            if (i == s.length() - 1)
                return i + 1;
        }

        return -1;
    }

    static boolean isBlank(char c) {
        /*
         * cはASCIIコードの範囲の文字（文字コードが0から127）とする cが空白文字（スペースまたはタブ）かどうかを返す
         */
        return c == ' ' || c == '\t';
    }

    static boolean isAlphabet(char c) {
        /*
         * cはASCIIコードの範囲の文字（文字コードが0から127）とする
         * cがアルファベット（大文字の'A'から'Z'、小文字の'a'から'z'、'_'）かどうかを返す
         */
        int t = (int) c;
        if ((t >= 65 && t <= 90) || (t >= 97 && t <= 122) || t == 95)
            return true;

        return false;
    }

    static boolean isDigit(char c) {
        /*
         * cはASCIIコードの範囲の文字（文字コードが0から127）とする cが数字（'0'から'9'）かどうかを返す
         */
        int t = (int) c;
        if (t >= 48 && t <= 57)
            return true;

        return false;
    }

    static boolean isAlphabetOrDigit(char c) {
        /*
         * cはASCIIコードの範囲の文字（文字コードが0から127）とする cがアルファベットまたは数字かどうかを返す
         */
        return isAlphabet(c) || isDigit(c);
    }

    static boolean isSymbol(char c) {
        /*
         * cはASCIIコードの範囲の文字（文字コードが0から127）とする cがJackのシンボルかどうかを返す
         * Jackのシンボルは常に1文字で、下記のいずれかの文字 {}()[].,;+-/*&|<>=~
         */
        int t = (int) c;
        if ((t >= 123 && t <= 126) || (t >= 40 && t <= 47) || t == 91 || t == 93 || t == 46 || t == 47
                || (t >= 59 && t <= 62) || t == 38)
            return true;

        return false;
    }

    static boolean isKeyword(String s) {
        /*
         * 文字列sがJackのキーワード（予約語）かどうかを返す
         */
        return keywordSet.contains(s);
    }

    private static Set<String> keywordSet = createKeywordSet();

    private static Set<String> createKeywordSet() {
        Set<String> set = new HashSet<String>();
        Collections.addAll(set, "class", "method", "function", "constructor", "int", "boolean", "char", "void", "var",
                "static", "field", "let", "do", "if", "else", "while", "return", "true", "false", "null", "this");
        return set;
    }
}

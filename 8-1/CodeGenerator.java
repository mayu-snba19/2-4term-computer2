import java.util.*;

class CodeGenerator {
    private UniqueLabelGenerator labelGererator = new UniqueLabelGenerator();

    // variable table
    private VarTable staticVarTable = new VarTable();
    private VarTable fieldVarTable = new VarTable();
    private VarTable parameterVarTable = new VarTable();
    private VarTable localVarTable = new VarTable();
    void defineVariable(String kind, String name, String type) {
        if (kind.equals("static")) {
            String segment = "static";
            staticVarTable.define(name, type, segment);
        } else if (kind.equals("field")) {
            String segment = "this";
            fieldVarTable.define(name, type, segment);
        } else if (kind.equals("parameter")) {
            String segment = "argument";
            parameterVarTable.define(name, type, segment);
        } else if (kind.equals("var")) {
            String segment = "local";
            localVarTable.define(name, type, segment);
        }
    }
    private VarInfo lookupVariable(String name) {
        if (localVarTable.isDefined(name)) {
            return localVarTable.get(name);
        } else if (parameterVarTable.isDefined(name)) {
            VarInfo varInfo = parameterVarTable.get(name);
            if (subroutineKindInCompilation.equals("method")) {
                return varInfo.adjustIndex(1);
            } else { // constructor, function
                return varInfo;
            }
        } else if (fieldVarTable.isDefined(name)) {
            return fieldVarTable.get(name);
        } else if (staticVarTable.isDefined(name)) {
            return staticVarTable.get(name);
        } else {
            return null;
        }
    }
    private int nLocalVar() {
        return localVarTable.size();
    }
    private int nParameterVar() {
        return parameterVarTable.size();
    }
    private int nFieldVar() {
        return fieldVarTable.size();
    }
    private int nStaticVar() {
        return staticVarTable.size();
    }

    private String classNameInCompilation;
    void startClass(String name) {
        classNameInCompilation = name;
        staticVarTable.clear();
        fieldVarTable.clear();
    }
    private String subroutineKindInCompilation;
    void startSubroutine(String kind) {
        subroutineKindInCompilation = kind;
        parameterVarTable.clear();
        localVarTable.clear();
    }

    // code generation
    VmCode subroutineDec(VmCode headCode, VmCode bodyCode) {
        VmCode code = new VmCode();
        code.addVmCode(headCode);
        code.addVmCode(bodyCode);
        return code;
    }
    VmCode subroutineHead(String subroutineName) {
        VmCode code = new VmCode();
        code.functionCmd(classNameInCompilation+"."+subroutineName, nLocalVar());
        if (subroutineKindInCompilation.equals("constructor")) {
            code.pushCmd("constant", nFieldVar());
            code.callCmd("Memory.alloc", 1);
            code.popCmd("pointer", 0);
        } else if (subroutineKindInCompilation.equals("method")) {
            code.pushCmd("argument", 0);
            code.popCmd("pointer", 0);            
        }
        return code;
    }
    VmCode letStatement(String varName, VmCode indexCode, VmCode exprCode) {
        VmCode code = new VmCode();
        code.addVmCode(exprCode);
        if (indexCode == null) {
            // 普通の変数
            // 課題
        } else {
            // 配列要素
            code.addVmCode(setThatPointer(varName, indexCode));
            code.popCmd("that", 0);
        }
        return code;
    }
    private VmCode setThatPointer(String varName, VmCode index) {
        VmCode code = new VmCode();
        VarInfo varInfo = lookupVariable(varName);
        code.pushCmd(varInfo.segment(), varInfo.index());
        code.addVmCode(index);
        code.addCmd();
        code.popCmd("pointer", 1);
        return code;
    }
    VmCode ifStatement(VmCode exprCode, VmCode thenCode, VmCode elseCode) {
        String trueLabel = labelGererator.newLabel("IF_TRUE");
        String falseLabel = labelGererator.newLabel("IF_FALSE");
        String endLabel = labelGererator.newLabel("IF_END");
        VmCode code = new VmCode();
        if (elseCode == null) {
            // elseがない場合
            // 課題
        } else {
            // elseがある場合
            // 課題
        }
        return code;
    }
    VmCode whileStatement(VmCode expr, VmCode statements) {
        String expLabel = labelGererator.newLabel("WHILE_EXP");
        String endLabel = labelGererator.newLabel("WHILE_END");
        VmCode code = new VmCode();
        code.labelCmd(expLabel);
        code.addVmCode(expr);
        code.notCmd();
        code.if_gotoCmd(endLabel);
        code.addVmCode(statements);
        code.gotoCmd(expLabel);
        code.labelCmd(endLabel);
        return code;
    }
    VmCode doStatement(VmCode subroutineCall) {
        VmCode code = new VmCode();
        code.addVmCode(subroutineCall);
        code.popCmd("temp", 0);
        return code;
    }
    VmCode returnStatement(VmCode expression) {
        VmCode code = new VmCode();
        if (expression == null) {
            code.pushCmd("constant", 0);
        } else {
            code.addVmCode(expression);
        }
        code.returnCmd();
        return code;
    }
    VmCode integerConstant(String integerConstant) {
        VmCode code = new VmCode();
        int index = Integer.parseInt(integerConstant);
        code.pushCmd("constant", index);
        return code;
    }
    VmCode stringConstant(String stringConstant) {
        VmCode code = new VmCode();
        String s = trimDoubleQuotes(stringConstant);
        code.pushCmd("constant", s.length());
        code.callCmd("String.new", 1);
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);
            code.pushCmd("constant", c);
            code.callCmd("String.appendChar", 2);
        }
        return code;
    }
    private String trimDoubleQuotes(String s) {
        return s.substring(1,s.length() - 1);
    }
    VmCode pushVariable(String varName) {
        VmCode code = new VmCode();
        VarInfo varInfo = lookupVariable(varName);
        code.pushCmd(varInfo.segment(), varInfo.index());
        return code;
    }
    VmCode pushArrayElement(String varName, VmCode index) {
        VmCode code = new VmCode();
        code.addVmCode(setThatPointer(varName, index));
        code.pushCmd("that", 0);
        return code;
    }
    VmCode subroutineCall(String classOrVarName, String subroutineName, List<VmCode> expressionList) {
        int nArgs = expressionList.size();
        VmCode code = new VmCode();
        if (classOrVarName == null) {
            code.pushCmd("pointer", 0);
            code.addVmCode(expressionList);
            code.callCmd(classNameInCompilation+"."+subroutineName, nArgs + 1);
        } else {
            VarInfo varInfo = lookupVariable(classOrVarName);
            if (varInfo != null) {
                String className = varInfo.type();
                code.addVmCode(pushVariable(classOrVarName));
                code.addVmCode(expressionList);
                code.callCmd(className+"."+subroutineName, nArgs + 1);
            } else { // className
                code.addVmCode(expressionList);
                code.callCmd(classOrVarName+"."+subroutineName, nArgs);
            }
        }
        return code;
    }
    VmCode op(String op) {
        VmCode code = new VmCode();
        if (op.equals("+")) {
            code.addCmd();
        } else if (op.equals("-")) {
            code.subCmd();
        } else if (op.equals("*")) {
            code.callCmd("Math.multiply",2);
        } else if (op.equals("/")) {
            code.callCmd("Math.divide",2);
        } else if (op.equals("&")) {
            code.andCmd();
        } else if (op.equals("|")) {
            code.orCmd();
        } else if (op.equals("<")) {
            code.ltCmd();
        } else if (op.equals(">")) {
            code.gtCmd();
        } else if (op.equals("=")) {
            code.eqCmd();
        }
        return code;
    }
    VmCode unaryOp(String op, VmCode termCode) {
        VmCode code = new VmCode();
        code.addVmCode(termCode);
        if (op.equals("-")) {
            code.negCmd();
        } else if (op.equals("~")) {
            code.notCmd();
        }
        return code;
    }
    VmCode keywordConstant(String k) {
        VmCode code = new VmCode();
        if (k.equals("true")) {
            code.pushCmd("constant", 0);
            code.notCmd();
        } else if (k.equals("false")) {
            code.pushCmd("constant", 0);
        } else if (k.equals("null")) {
            code.pushCmd("constant", 0);
        } else if (k.equals("this")) {
            code.pushCmd("pointer", 0);
        }
        return code;
    }
}
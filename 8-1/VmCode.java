import java.util.*;

class VmCode {
    private List<String> codeList = new ArrayList<String>();

    public List<String> getCodeList() {
        return codeList;
    }

    public String toString() {
        return String.join("\n", codeList);
    }

    public void addVmCode(VmCode code) {
        codeList.addAll(code.getCodeList());
    }

    public void addVmCode(List<VmCode> codeList) {
        for (VmCode code : codeList) {
            addVmCode(code);
        }
    }

    public void addCmd() {
        codeList.add("add");
    }

    public void subCmd() {
        codeList.add("sub");
    }

    public void negCmd() {
        codeList.add("neg");
    }

    public void eqCmd() {
        codeList.add("eq");
    }

    public void gtCmd() {
        codeList.add("gt");
    }

    public void ltCmd() {
        codeList.add("lt");
    }

    public void andCmd() {
        codeList.add("and");
    }

    public void orCmd() {
        codeList.add("or");
    }

    public void notCmd() {
        codeList.add("not");
    }

    public void pushCmd(String segment, int index) {
        codeList.add("push " + segment + " " + index);
    }

    public void popCmd(String segment, int index) {
        codeList.add("pop " + segment + " " + index);
    }

    public void labelCmd(String label) {
        codeList.add("label " + label);
    }

    public void if_gotoCmd(String label) {
        codeList.add("if-goto " + label);
    }

    public void gotoCmd(String label) {
        codeList.add("goto " + label);
    }

    public void functionCmd(String functionName, int nLocals) {
        codeList.add("function " + functionName + " " + nLocals);
    }

    public void callCmd(String functionName, int nArgs) {
        codeList.add("call " + functionName + " " + nArgs);
    }

    public void returnCmd() {
        codeList.add("return");
    }
}

class VarInfo {
    private String name;
    private String type;
    private String segment;
    private int index;
    public VarInfo(String n, String t, String s, int i) {
        name = n;
        type = t;
        segment = s;
        index = i;
    }
    public String name() {
        return name;
    }
    public String type() {
        return type;
    }
    public String segment() {
        return segment;
    }
    public int index() {
        return index;
    }
    public VarInfo adjustIndex(int d) {
        return new VarInfo(name, type, segment, index + d);
    }
    public String toString() {
        return String.join(",", name, type, segment,  ""+index);
    }
}
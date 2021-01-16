import java.util.*;

class VarTable {
    private Map<String,VarInfo> table = new HashMap<String,VarInfo>();
    public boolean isDefined(String name) {
        return table.containsKey(name);
    }
    public void define(String name, String type, String segment) {
        if (!isDefined(name)) {
            int index = table.size();
            table.put(name, new VarInfo(name, type, segment, index));
        }
    }
    public VarInfo get(String name) {
        return table.get(name);
    }
    public int size() {
        return table.size();
    }
    public void clear() {
        table.clear();
    }
    public String toString() {
        return table.toString();
    }
}
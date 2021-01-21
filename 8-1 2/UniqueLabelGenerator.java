class UniqueLabelGenerator {
    private int nextNo;
    public UniqueLabelGenerator() {
        nextNo = 0;
    }
    public String newLabel(String prefix) {
        String label = prefix+nextNo;
        nextNo++;
        return label;
    } 
}
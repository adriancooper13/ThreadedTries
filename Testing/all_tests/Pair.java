package testing.all_tests;

public class Pair {
    Operation op;
    int index;

    public Pair(int val, int index) {
        switch (val) {
            case 0:
                op = Operation.add;
                break;
            case 1:
                op = Operation.remove;
                break;
            default:    
                op = Operation.contains;
                break;
        }
        this.index = index;
    }

    @Override
    public String toString() {
        return "Operation: " + op + ", Index: " + index + String.format("%n");
    }
}
import java.util.*;
import java.io.File;

class DifferentTrieThread extends Thread {

    public static final OptimisticTrie root = new OptimisticTrie();
    public Operation op;
    public String s;

    public DifferentTrieThread(int threadNumber) {
        super(String.valueOf(threadNumber));
    }

    @Override
    public void run() {
        boolean res = false;
        switch (op) {
            case add:
                res = root.insert(s);
                break;
            case remove:
                res = root.remove(s);
                break;
            case search:
                res = root.search(s);
                break;
        }

        StringBuilder str = new StringBuilder();
        str.append("Thread").append(" ");
        str.append(super.getName()).append(" ");
        str.append("calling").append(" ").append(op.name()).append("()");
        str.append(": ").append(op.name()).append(" ").append(s);

        System.out.println(str);
    }

    public void set(Operation op, String s) {
        this.op = op;
        this.s = s;
    }
}


public class TestOptimisticInOrder {

    public static void main(String[] args) throws Exception {

        DifferentTrieThread adds = new DifferentTrieThread(0);
        DifferentTrieThread deletes = new DifferentTrieThread(1);

        Scanner input = new Scanner(new File("dict.txt"));
        Random rand = new Random();

        ArrayList<String> words = new ArrayList<>();
        while (input.hasNextLine())
            words.add(input.nextLine());

        for (String s : words) {
            adds.set(Operation.add, s);
            deletes.set(Operation.remove, s);

            adds.run();
            deletes.run();
        }

        System.out.println(DifferentTrieThread.root.size());
    }
}

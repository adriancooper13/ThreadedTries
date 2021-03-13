import java.io.File;
import java.util.*;

enum Operation {
    add, remove, search
}

class TrieThread extends Thread {

    public static final OptimisticTrie root = new OptimisticTrie();
    public Operation op;
    public String s;

    public TrieThread(int threadNumber) {
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
                if (res) TestOptimisticWithRandom.deletes += 1;
                break;
            case search:
                res = root.search(s);
                break;
        }

        if ((op == Operation.add || op == Operation.remove) && !res)
            return;

        StringBuilder str = new StringBuilder();
        str.append("Thread").append(" ");
        str.append(super.getName()).append(" ");
        str.append("calling").append(" ").append(op.name()).append("()");
        str.append(": ").append(res);

        System.out.println(str);
    }

    public void set(Operation op, String s) {
        this.op = op;
        this.s = s;
    }
}

public class TestOptimisticWithRandom {

    public static int deletes = 0;

    public static void main(String[] args) throws Exception {
        
        int numThreads = args.length == 0 ? 1 : Integer.valueOf(args[0]);
        TrieThread[] threads = new TrieThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new TrieThread(i);
        }

        Scanner ifp = new Scanner(new File("dict.txt"));
        Random rand = new Random();
        ArrayList<String> words = new ArrayList<>();
        
        while (ifp.hasNextLine())
            words.add(ifp.nextLine());
        
        ifp.close();
        ifp = null;

        int count = 0;
        boolean[] wasAdded = new boolean[words.size()];
        while (count < words.size()) {
            int thread;
            do {
                thread = rand.nextInt(numThreads);
            } while (threads[thread].isAlive());

            Operation op;
            switch (rand.nextInt(3)) {
                case 0:
                    op = Operation.add;
                    break;
                case 1:
                    op = Operation.remove;
                    break;
                default:
                    op = Operation.search;
                    break;
            }

            int index = rand.nextInt(words.size());
            if (op == Operation.add && !wasAdded[index]) {
                wasAdded[index] = true;
                count += 1;
            }

            threads[thread].set(op, words.get(index));
            threads[thread].run();
        }

        for (TrieThread t : threads)
            t.join();

        //System.out.println();
        //System.out.println("Size should be: " + (words.size() - deletes));
        //System.out.println("Actual size: " + TrieThread.root.size());

    }
}
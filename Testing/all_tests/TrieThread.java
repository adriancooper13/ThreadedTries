package testing.all_tests;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import tries.*;

public class TrieThread extends Thread {

    // Shared trie for all threads.
    public static Trie trie;
    public static final HashMap<Integer, String> words = new HashMap<>();
    public static final HashMap<String, Boolean> exists = new HashMap<>();
    public static AtomicInteger adds, deletes;
    private Operation op;
    private int index;

    public TrieThread(String version, int val) {
        super(String.valueOf(val));

        if (trie == null) {
            switch (version) {
                case "Optimistic":
                    trie = new OptimisticTrie();
                    break;
                // case "FineGrain":
                //     trie = new FineGrainTrie();
                //     break;
                case "WaitFree":
                    trie = new WaitFreeTrie();
                    break;
            }
        }

        if (adds == null) adds = new AtomicInteger(0);
        if (deletes == null) deletes = new AtomicInteger(0);
    }

    public void prepare(Pair p) {
        index = p.index;
        op = p.op;
    }

    @Override
    public void run() {
        String s = words.get(index);
        boolean res;
        try {
            switch (op) {
                case add:
                    res = trie.add(s);
                    if (res == exists.get(s))
                        throw new Exception("Something went wrong with adding");

                    if (res) {
                        exists.put(s, true);
                        adds.getAndIncrement();
                    }
                    break;
                case remove:
                    res = trie.remove(s);
                    if (res != exists.get(s))
                        throw new Exception("Something went wrong with removing");

                    if (res) {
                        exists.put(s, false);
                        deletes.getAndIncrement();
                    }
                    break;
                default:
                    if (trie.contains(s) != exists.get(s))
                        throw new Exception("Something went wrong with contains");
                    break;
            }
        }
        catch (Exception e) {
            System.out.println();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
package tries;

import tries.*;

public class WaitFreeTrie extends Trie {

    static final int ALPHA_SIZE = 26;
    TrieNode root;

    public WaitFreeTrie() {
        root = new TrieNode();
    }

    public class TrieNode {
        TrieNode [] children;
        boolean flag;

        public TrieNode() {
            children = new TrieNode[ALPHA_SIZE];
            flag = false;
        }
    }

    public boolean add(String key) {
        int len = key.length();
        TrieNode current = root;

        for (int i = 0; i < len; i++) {
            int temp = key.charAt(i) - 'a';

            if (current.children[temp] == null) {
                current.children[temp] = new TrieNode();
            }

            current = current.children[temp];
        }

        if (!current.flag) {
            current.flag = true;
            size.getAndIncrement();
            return true;
        }

        return false;
    }

    public boolean contains(String key) {
        int len = key.length();
        TrieNode current = root;

        for (int i = 0; i < len; i++) {
            int temp = key.charAt(i) - 'a';

            if (current.children[temp] == null) {
                return false;
            }

            current = current.children[temp];
        }

        return current.flag;
    }

    public boolean remove(String key) {
        int len = key.length();
        TrieNode current = root;

        for (int i = 0; i < len; i++) {
            int temp = key.charAt(i) - 'a';

            if (current.children[temp] == null) {
                return false;
            }

            current = current.children[temp];
        }

        if (current.flag) {
            current.flag = false;
            size.getAndDecrement();
            return true;
        }

        return false;
    }
}


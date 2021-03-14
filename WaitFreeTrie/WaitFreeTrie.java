public class WaitFreeTrie {

    static final int ALPHA_SIZE = 26;
    TrieNode root;
    int size;

    public WaitFreeTrie() {
        size = 0;
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

    public int size() {
        return size;
    }

    public boolean insert(String key) {
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
            size += 1;
            return true;
        }

        return false;
    }

    public boolean search(String key) {
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
            size -= 1;
            return true;
        }

        return false;
    }
}


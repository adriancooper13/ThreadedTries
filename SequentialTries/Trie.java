import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Trie {
    private int size;
    private Node root;

    public Trie() {
        size = 0;
        root = new Node();
    }

    public Trie(Collection<String> c) {
        this();
        addAll(c);
    }

    private class Node {
        public int count;
        public HashMap<Character, Node> children;

        public Node() {
            count = 0;
            children = new HashMap<>();
        }

        public boolean remove(String word, int index) {

            if (index == word.length()) {
                count -= 1;
                size -= 1;
                if (children.size() <= 0)
                    children = null;
                return true;
            }

            if (remove(word, index + 1)) {
                for (Map.Entry<Character, Node> e : children.entrySet()) {
                    if (e.getValue().children == null)
                        children.remove(e.getKey());
                }
                if (children.size() <= 0)
                    children = null;
                return true;
            }

            return false;
        }
    }

    public boolean add(String word) throws NullPointerException {
        if (word == null)
            throw new NullPointerException();

        int len = word.length();
        Node current = root;

        try {
            for (int index = 0; index < len; index++) {
                char c = word.charAt(index);
                if (!current.children.containsKey(c))
                    current.children.put(c, new Node());
                current = current.children.get(c);
            }
        } catch (Exception e) {
            return false;
        }

        current.count += 1;
        size += 1;
        return true;
    }

    public boolean addAll(Collection<String> c) throws NullPointerException {
        if (c == null)
            throw new NullPointerException();

        boolean wasChanged = false;
        for (String s : c) {
            if (add(s))
                wasChanged = true;
        }

        return wasChanged;
    }

    public boolean contains(String word) throws NullPointerException {
        if (word == null)
            throw new NullPointerException();

        int len = word.length();
        Node current = root;

        for (int index = 0; index < len; index++) {
            char c = word.charAt(index);
            if (!current.children.containsKey(c))
                return false;
            current = current.children.get(c);
        }

        return current.count > 0;
    }

    public boolean containsAll(Collection<String> c) throws NullPointerException {
        if (c == null)
            throw new NullPointerException();

        for (String s : c) {
            if (!contains(s))
                return false;
        }

        return true;
    }

    public boolean remove(String word) throws NullPointerException {
        if (word == null)
            throw new NullPointerException();
        return root.remove(word, 0);
    }

    public boolean removeAll(Collection<String> c) throws NullPointerException {
        if (c == null)
            throw new NullPointerException();

        boolean wasChanged = false;
        for (String s : c) {
            if (remove(s))
                wasChanged = true;
        }

        return wasChanged;
    }

    public int size() {
        return size;
    }
}
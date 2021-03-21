package all_tests;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class OptimisticTrie extends Trie {
    
    private Node root;
    // private AtomicInteger size;

    public OptimisticTrie() {
        root = new Node();
    }

    public OptimisticTrie(Collection<String> c) {
        this();
        addAll(c);
    }

    private class Node {
        AtomicBoolean isWord;
        ConcurrentHashMap<Character, Node> children;

        public Node() {
            isWord = new AtomicBoolean(false);
            children = new ConcurrentHashMap<>();
        }

        public boolean cleanup() {
            // Base Case: if there are no children and this is not a word, return
            // true so we can delete this node.
            if (children.isEmpty())
                return !isWord.get();

            // Keep track of all the nodes that need to be deleted.
            ArrayDeque<Character> toDelete = new ArrayDeque<>();
            // Loop through all the words DFS style to cleanup
            for (Map.Entry<Character, Node> e : children.entrySet()) {
                if (e.getValue().cleanup())
                    toDelete.add(e.getKey());
            }

            // Physically delete nodes
            for (Character c : toDelete)
                children.remove(c);

            // Return if this node can now be deleted.
            return children.isEmpty() && !isWord.get();
        }
    }

    /**
     * Attempts to add s to the trie. Runs in O(k) time where k is the length of s
     * @param s The string to be added.
     * @return true if and only if s was not already in the trie. Otherwise false.
     */
    public boolean add(String s) {
        Node current = root;
        int len = s.length();

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            current.children.putIfAbsent(c, new Node());
            current = current.children.get(c);
        }

        if (!current.isWord.getAndSet(true)) {
            size.getAndIncrement();
            return true;
        }

        return false;
    }

    /**
     * Attempts to add all Strings in c to the trie.
     * @param c The collection containing all the Strings to be added
     * @return true if and only if the trie changes as a result of this method call.
     * Otherwise false.
     */
    public boolean addAll(Collection<String> c) {
        boolean hasChanged = false;
        for (String s : c) 
            if (add(s))
                hasChanged = true;
        return hasChanged;
    }

    /**
     * Checks to see if the trie contains s
     * @param s The string to check
     * @return true if and only if s is in the trie. Otherwise false.
     */
    public boolean contains(String s) {
        Node current = root;
        int len = s.length();

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (!current.children.containsKey(c))
                return false;
            current = current.children.get(c);
        }

        return current.isWord.get();
    }

    public boolean remove(String s) {
        Node current = root;
        int len = s.length();

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (!current.children.containsKey(c))
                return false;
            current = current.children.get(c);
        }

        if (!current.isWord.getAndSet(false))
            return false;
        
        size.getAndDecrement();
        return true;
    }

    // public int size() {
    //     return size.get();
    // }

    @Override
    public String toString() {
        ArrayList<String> words = new ArrayList<>(size.get());
        getAll(root, new StringBuilder(), words);
        Collections.sort(words);
        return words.toString();
    }

    @Override
    public boolean equals(Object o) {
        OptimisticTrie other = (OptimisticTrie)o;
        return (toString().equals(other.toString()));
    }

    private void getAll(Node current, StringBuilder str, ArrayList<String> words) {
        if (current == null) return;
        if (current.isWord.get()) words.add(str.toString());

        for (Map.Entry<Character, Node> e : current.children.entrySet()) {
            str.append(e.getKey());
            getAll(e.getValue(), str, words);
            str.deleteCharAt(str.length() - 1);
        }
    }
}
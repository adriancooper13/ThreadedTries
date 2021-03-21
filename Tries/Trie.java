package Tries;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Trie {
    protected AtomicInteger size;

    public Trie() {
        size = new AtomicInteger(0);
    }
    
    public int size() {
        return size.get();
    }

    public abstract boolean add(String s);

    public abstract boolean remove(String s);

    public abstract boolean contains(String s);

}
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please provide a filename when running the program.");
            return;
        }

        Scanner in = new Scanner(new File(args[0]));
        ArrayList<String> arr = new ArrayList<>(84_100);
        while (in.hasNextLine()) {
            arr.add(in.nextLine());
        }
        in.close();

        Trie t = new Trie();
        t.addAll(arr);

        System.out.println(t.size());
        System.out.println(t.remove("abactor"));
        System.out.println(t.size());
    }
}
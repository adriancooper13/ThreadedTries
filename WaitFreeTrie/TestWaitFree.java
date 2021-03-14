import java.util.*;
import java.io.*;

public class TestWaitFree {
    public static void main (String[] args) throws Exception {
        WaitFreeTrie test = new WaitFreeTrie();

        Scanner scan = new Scanner(new File("dict.txt"));
        ArrayList<String> dict = new ArrayList<>();

        while (scan.hasNextLine()) {
            String temp  = scan.nextLine();
            dict.add(temp);
            test.insert(temp);
        }

        System.out.println(dict.size());

        for (String temp: dict) {
            test.remove(temp);
        }
    }
}

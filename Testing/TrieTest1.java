package testing;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import testing.all_tests.*;
import tries.*;

public class TrieTest1 {

    public static void main(String[] args) throws Exception {
        
        // Read file and put all the words in the hashmaps
        Scanner input = new Scanner(new File("testing/all_tests/engmix.txt"));
        for (int i = 0; input.hasNextLine(); i++) {
            String s = input.nextLine();
            TrieThread.words.put(i, s);
            TrieThread.exists.put(s, false);
        }
        input.close();
        final int NUM_WORDS = TrieThread.words.size();
        
        // Get random number of operations.
        Random rand = new Random();
        Pair[] pairs = new Pair[rand.nextInt(400_000) + 100_000];
        for (int i = 0; i < pairs.length; i++)
            pairs[i] = new Pair(rand.nextInt(3), rand.nextInt(NUM_WORDS));

            String[] versions = args.clone();
            
            // Run test case for each version of the trie.
        for (String version : versions) {

            StringBuilder json = new StringBuilder();
                
            System.out.println();
            System.out.println("Testing " + version + "Trie with " + pairs.length + " operations");
            System.out.println("==============================");
        
            // Run tests for number of threads that are powers of 2 i.e. 1, 2, 4, 8, ... , 32
            for (int numThreads = 1; numThreads <= 32; numThreads *= 2) {

                // Separate json objects if there has been one created.
                if (numThreads > 1)
                    json.append(String.format("%n"));

                // Start creating the json object with the number of threads.
                json.append("threads : ").append(numThreads).append(",");

                System.out.println("Running " + numThreads + " thread(s)");
                
                // Create the threads.
                TrieThread[] threads = new TrieThread[numThreads];
                for (int i = 0; i < numThreads; i++)
                    threads[i] = new TrieThread(version, i + 1);

                // Start the timer.
                long start = System.currentTimeMillis();

                // Run each operation.
                for (int i = 0; i < pairs.length; i++) {
                    threads[i % numThreads].prepare(pairs[i]);
                    threads[i % numThreads].run();
                }
                
                // Wait for all threads to finish.
                for (TrieThread thread : threads)
                    thread.join();

                // End timer.
                long end = System.currentTimeMillis();

                // Finish creating the json object with the execution time.
                json.append("time").append(" : ").append(end - start);

            }
            
            // Create output file and write json object to it.
            PrintWriter output = new PrintWriter(new File(version + "json.txt"));
            output.println(json);
            output.close();
        }
    }
}
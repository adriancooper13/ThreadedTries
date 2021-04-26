# Threaded Tries

These are the files that have been created so far in our project.
Right now there are three important files that represent each section of the project:

## Optimistic Trie

This trie uses a semi-optimistic approach. It makes use of tools already available in the java util package, such as AtomicBoolean, AtomicInteger, and ConcurrentHashMap.

## Lock-Free Trie

This trie uses a completely lock free approach. Right now, it has no methods that guarantee it will run correctly. We wanted to include this because of nature of tries and how they can branch out rather quickly with the vast number of words.

## Fine-Grain Trie

This trie uses a hand over hand locking mechanism to ensure thread safety across the entire data structure.

### Testing

All three implementations are tested using the TrieTest1.java within the testing directory. This test creates a random number of operations to be performed between 100,000 and 500,000. It then randomly selects which operations get performed and in what order, and this selection will be the same for all three implementations.

# How to Run

You can run the test cases two different ways:
	1. Run the Jupyter Notebook
		CAUTION: The Jupyter Notebook does make some system calls within the the ThreadedTries directory to read files and delete .class files. If you are not okay with this happening on your machine, you can either run it on a VM or just look at the last run currently in the notebook.

		- In the third cell there is a variable named "number". This is the test case that is 	being ran. You can either make this 1 or 2 as there are only two test cases.

	2. Compile and run the java test cases from command line
		Note: This will not produce any graphs, but there is some output to the terminal to indicate something is actually happening as well as a text file that shows the runtime in milliseconds paired with the number of threads. This is also a one and done testcase.

		a. Navigate to the ThreadedTries directory in terminal.
		b. Compile: ```javac testing/TrieTest1.java``` or ```javac testing/TrieTest2.java```
		c. This file takes command line arguments. You must include at least one of the trie implementations as 	a command line argument. You may include up to all three.
		c. Run: ```java testing/TrieTest1 Optimistic FineGrain WaitFree``` or ```java testing/TrieTest2 Optimistic FineGrain WaitFree```
			- This will do one run for all three implementations, and produce three separate text files displaying the results.
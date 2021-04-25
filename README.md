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

In order to run the test case, you can open the Jupyter Notebook file and just run all cells.

** PLEASE NOTE **
The Jupyter Notebook does make some system calls within the the ThreadedTries directory. If you are not okay with this happening on your machine, you can either run it on a VM or just look at the last run currently in the notebook.
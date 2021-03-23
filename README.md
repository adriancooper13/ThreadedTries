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

As of now, the Fine-Grain trie has not been tested. However, both the Optimistic and Lock-Free tries are being tested and compared in the same enviornment with the differences shown in the graph directory.
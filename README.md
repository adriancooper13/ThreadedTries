# Threaded Tries

These are the files that have been created so far in our project.
Right now there are three important files that represent each section of the project:

## Optimistic Trie

This trie uses a semi-optimistic approach. It makes use of tools already available in the java util package, such as AtomicBoolean, AtomicInteger, and ConcurrentHashMap.

### Testing
Only basic testing has been completed at this point. There will be a more comprehensive guide to how testing was performed in the final project along with the testing files.

## Lock-Free Trie

This trie uses a completely lock free approach. Right now, it has no methods that guarantee it will run correctly. We wanted to include this because of nature of tries and how they can branch out rather quickly with the vast number of words.

### Testing

Only basic testing has been completed at this point. There will be a more comprehensive guide to how testing was performed in the final project along with the testing files.

## Fine-Grain Trie

This trie uses a hand over hand locking mechanism to ensure thread safety across the entire data structure.

### Testing

No testing has been performed on this version yet.
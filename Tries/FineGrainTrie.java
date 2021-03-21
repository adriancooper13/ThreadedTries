import java.util.*;
import java.util.concurrent.locks.*;

// This is a fine-grained trie data structure, it only has add, contains, remove, and size methods
public class TrieFineGrained extends Trie
{
	private Node root;

	// constructer for this class, creats a root node and sets the size to zero
	public TrieFineGrained()
	{
		root = new Node();
	}

	// This adds a word s to the trie and increases the size, it locks and unlocks hand-over-hand fasion
	public boolean add(String s)
	{
		Node current = root;
		int len = s.length();
		Lock[] keyring = new Lock[2];

		// adds one char at a time to trie
		for (int i = 0; i < len; i++) 
		{
			char c = s.charAt(i);
			// if there is no next node to go to then make one
			if (!current.isNodeAt(c))
				current.addNode(c);

			// provents unlocking of a non-existing lock
			if (keyring[i % 2] != null)
				keyring[i % 2].unlock();
	
			keyring[i % 2] = current.getlock();
			current = current.getNextNode(c);
		}

		current.setIsWord(true);

		for (Lock l: keyring) 
		{
			l.unlock();	
		}

		size.getAndIncrement();
		return true;
	}

	// This goes through the trie and tries to find a word s and if there is no path or the flag for a word
	// is not there then it returns false, this does not lock
	public boolean contains(String s)
	{
		Node current = root;
		int len = s.length();

		for (int i = 0; i < len; i++) 
		{
			char c = s.charAt(i);
			if (!current.isNodeAt(c))
				return false;

			current = current.getNextNodeNonLock(c);
		}

		return current.isWord();
	}

	// Same thing as add but it just goes through a finds the last node and changes the flag to a false
	public boolean remove(String s)
	{
		Node current = root;
		int len = s.length();
		Lock[] keyring = new Lock[2];

		for (int i = 0; i < len; i++) 
		{
			char c = s.charAt(i);
			// this cheaks to see if the word is acually in there
			if (!current.isNodeAt(c))
				return false;

			if (keyring[i % 2] != NULL)
				keyring[i % 2].unlock();

			keyring[i % 2] = current.getlock();
			current = current.getNextNode(c);
		}

		current.setIsWord(false);

		for (Lock l: keyring) 
		{
			l.unlock();	
		}

		size.getAndDecrement();
		// This cleans any pieces of the left over trie so there is no reduntent space
		cleanup(s);

		return true;
	}

	// goes over the area that had the word that was removed and gets rid of any empty nodes
	private void cleanup(String s)
	{
		Node current = root;
		int len = s.length();
		Lock[] keyring = new Lock[2];

		// This looks at all layers of the word that was deleted for throughness
		for (int x = 0; x < len; x++) 
		{	
			for (int i = 0; i < len; i++) 
			{
				char c = s.charAt(i);
				if (!current.isNodeAt(c))
					return false;

				if (current.getNextNodeNonLock(c).isEmpty())
				{
					current.deleteNode(c);
					for (Lock l: keyring) 
					{
						if (l != NULL) 
						{
							l.unlock();	
						}
					}
					break;
				}

				if (keyring[i % 2] != NULL)
					keyring[i % 2].unlock();

				keyring[i % 2] = current.getlock();
				current = current.getNextNode(c);
			}
		}

		for (Lock l: keyring) 
		{
			if (l != NULL) 
			{
				l.unlock();	
			}
		}
	}

	public int size()
	{
		return size;
	}
}

private class nodeLock
{
	private boolean isWord;
	private Node[] alpha;
	private final Lock queueLock = new ReentrantLock(true);

	// Makes a node class
	public Node()
	{
		isWord = false;
		alpha = new Node[26];
	}

	public boolean isEmpty()
	{
		// makes sure it does not delete a leaf node that is a word
		if (isWord)
			return false;

		for (Node i : alpha) 
		{
			if (i != NULL)
				return false;	
		}

		return true;
	}

	public boolean isWord()
	{
		return isWord;
	}

	public boolean isNodeAt(int c)
	{
		if (alpha[c - 'a'] == NULL) 
		{
			return false;
		}
		return true;
	}

	public void addNode(int c)
	{
		Node[c - 'a'] = new Node();
	}

	public Lock getlock()
	{
		return queueLock;
	}

	public boolean releaseLock()
	{
		return queueLock.unlock();
	}

	public void setIsWord(boolean isWord)
	{
		this.isWord = isWord;
	}

	// This locks the next node and returns the node back to the trie
	// This prvents the whole list in the begining to be locked and its only the link that is locked
	public Node getNextNode(int c)
	{
		alpha[c - 'a'].getlock.lock;
		return alpha[c - 'a'];
	}

	public Node getNextNodeNonLock(int c)
	{
		return alpha[c - 'a'];
	}

	public void deleteNode(int c)
	{
		alpha[c - 'a'] = NULL;
	}

}
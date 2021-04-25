
import java.util.*;
import java.util.concurrent.locks.*;
import java.io.*;


public class testing
{
	public static void main(String[] args) 
	{
		Scanner input = new Scanner(new File("testing/all_tests/engmix.txt"));
        ArrayList<String> dic = new ArrayList<String>();

        for (int i = 0; input.hasNextLine(); i++) 
        {
            String s = input.nextLine();
            dic.add(s);
        }
        input.close();

        int length = dic.size();

        System.out.println("There are a total of " + length + " in the engmix.txt.");

        ArrayList<String> dicEven = new ArrayList<String>();
        ArrayList<String> dicOdd = new ArrayList<String>();

        for (int i = 0; i < length; i++) 
        {
        	if (i % 2 == 0) 
        	{
        		dicEven.add(dic.get(i));	
        	}
        	else
        	{
        		dicOdd.add(dic.get(i));	
        	}
        }

        System.out.println("In dicEven there is " + dicEven.size() + " words.");
        System.out.println("In dicOdd there is " + dicOdd.size() + " words.");


        Thread thread[] = new Thread[2]; // only 10 guests
        TrieFineGrained trieDic = new TrieFineGrained();


    	thread[1] = new Thread(new adding(trieDic, dicEven), "dicEven");
    	thread[2] = new Thread(new adding(trieDic, dicOdd), "dicOdd");

    	thread[1].start();
    	thread[2].start();

    	while(thread[1].isAlive() || thread[2].isAlive());

	}
}

class adding implements Runnable
{
   private TrieFineGrained trieDic;
   private ArrayList<String> myDic;
 
   public adding(TrieFineGrained trieDic, ArrayList<String> myDic)
   {
      this.trieDic = trieDic;
      this.myDic = myDic;
   }

   public void run()
   {
   		for (String word: myDic) 
   		{
   			trieDic.add(word);
   		}

   		System.out.println(Thread.currentThread().getName() + " is done adding to the trie.");
   }
}


// This is a fine-grained trie data structure, it only has add, contains, remove, and size methods
class TrieFineGrained
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

			if (keyring[i % 2] != null)
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
					return;

				if (current.getNextNodeNonLock(c).isEmpty())
				{
					current.deleteNode(c);
					for (Lock l: keyring) 
					{
						if (l != null) 
						{
							l.unlock();	
						}
					}
					break;
				}

				if (keyring[i % 2] != null)
					keyring[i % 2].unlock();

				keyring[i % 2] = current.getlock();
				current = current.getNextNode(c);
			}
		}

		for (Lock l: keyring) 
		{
			if (l != null) 
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

class Node
{
	private boolean isWord;
	private Node[] alpha;
	private final ReentrantLock queueLock = new ReentrantLock(true);

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
			if (i != null)
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
		if (alpha[c - 'a'] == null) 
		{
			return false;
		}
		return true;
	}

	public void addNode(int c)
	{
		alpha[c - 'a'] = new Node();
	}

	public Lock getlock()
	{
		return queueLock;
	}

	public boolean releaseLock()
	{
		queueLock.unlock();
		return queueLock.isLocked();
	}

	public void setIsWord(boolean isWord)
	{
		this.isWord = isWord;
	}

	// This locks the next node and returns the node back to the trie
	// This prvents the whole list in the begining to be locked and its only the link that is locked
	public Node getNextNode(int c)
	{
		alpha[c - 'a'].getlock().lock();
		return alpha[c - 'a'];
	}

	public Node getNextNodeNonLock(int c)
	{
		return alpha[c - 'a'];
	}

	public void deleteNode(int c)
	{
		alpha[c - 'a'] = null;
	}

}
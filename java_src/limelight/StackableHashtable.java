package limelight;

import java.util.*;

public class StackableHashtable
{
	private LinkedList<Hashtable<String, String>> stack;

	public StackableHashtable()
	{
		stack = new LinkedList<Hashtable<String, String>>();
		stack.addFirst(new Hashtable<String, String>());
	}

	public void push(Hashtable<String, String> table)
	{
		stack.addFirst(table);
	}

	public Hashtable<String, String> pop()
	{
		return stack.removeFirst();
	}

	public void addToBottom(Hashtable<String, String> table)
	{
		stack.addLast(table);
	}

	public Hashtable<String, String> removeFromBottom()
	{
		return stack.removeFirst();
	}

	public String get(String key)
	{
		String value = null;
		for(Iterator<Hashtable<String, String>> iterator = stack.iterator(); iterator.hasNext();)
		{
			Hashtable<String, String> table = iterator.next();
			value = table.get(key);
			if(value != null)
			 break;
		}
		return value;
	}

	public void put(String key, String value)
	{
		stack.getFirst().put(key, value);
	}
}

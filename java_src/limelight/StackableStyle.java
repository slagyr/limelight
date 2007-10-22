package limelight;

import java.util.*;

public class StackableStyle extends Style
{
	private LinkedList<Style> stack;

	public StackableStyle()
	{
		stack = new LinkedList<Style>();
		stack.addFirst(new Style());
	}

	protected String get(String key)
	{
		String value = null;
		for(Iterator iterator = stack.iterator(); iterator.hasNext();)
		{
			Style style = (Style) iterator.next();
			value = style.get(key);
			if(value != null)
			 break;
		}
		return value;
	}

	protected void put(String key, String value)
	{
		stack.getFirst().put(key, value);
	}

	public void push(Style style)
	{
		stack.addFirst(style);
	}

	public Style pop()
	{
		return stack.removeFirst();
	}

	public void addToBottom(Style style)
	{
		stack.addLast(style);
	}

	public Style removeFromBottom()
	{
		return stack.removeFirst();
	}
}

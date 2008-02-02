package limelight.ui;

import limelight.ui.Style;
import limelight.ui.FlatStyle;

import java.util.*;

public class StackableStyle extends Style
{
	private LinkedList<FlatStyle> stack;

	public StackableStyle()
	{
		stack = new LinkedList<FlatStyle>();
		stack.addFirst(new FlatStyle());
	}

	protected String get(String key)
	{
		String value = null;
    for (FlatStyle style : stack)
    {
      value = style.get(key);
      if (value != null)
        break;
    }
		return value;
	}

	protected void put(String key, String value)
	{
		stack.getFirst().put(key, value);
	}

  protected boolean has(Object key)
  {
    for (FlatStyle style : stack)
    {
      if(style.has(key))
        return true;
    }
    return false;
  }

  public int checksum()
  {
    return getReducedStyle().checksum();
  }

  public void push(FlatStyle style)
	{
		stack.addFirst(style);
	}

	public FlatStyle pop()
	{
		return stack.removeFirst();
	}

	public void addToBottom(FlatStyle style)
	{
		stack.addLast(style);
	}

	public FlatStyle removeFromBottom()
	{
		return stack.removeFirst();
	}

  public FlatStyle getReducedStyle()
  {
    FlatStyle reduced = new FlatStyle();
    for (FlatStyle style : stack)
    {
      for(Map.Entry entry : style.getStyles().entrySet())
      {
          if(!reduced.has(entry.getKey()))
            reduced.put((String)entry.getKey(), (String)entry.getValue());
      }
    }
    return reduced;
  }
}

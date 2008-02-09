package limelight.ui;

import limelight.Util;

import java.util.LinkedList;

public class StackableStyle extends Style implements StyleObserver
{
	private LinkedList<FlatStyle> stack;

  public StackableStyle()
	{
		stack = new LinkedList<FlatStyle>();
		push(new FlatStyle());
	}

	protected String get(int key)
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

	protected void put(int key, String value)
	{
		stack.getFirst().put(key, value);
	}

  protected boolean has(int key)
  {
    for (FlatStyle style : stack)
    {
      if(style.has(key))
        return true;
    }
    return false;
  }

  public void push(FlatStyle style)
	{
    applyChangesFromTop(style);
    stack.addFirst(style);
    style.addObserver(this);
  }

	public FlatStyle pop()
	{
    FlatStyle style = stack.removeFirst();
    applyChangesFromTop(style);
    style.removeObserver(this);
    return style;
	}

  public void addToBottom(FlatStyle style)
	{
    applyChangesFromBottom(style);
    stack.addLast(style);
    style.addObserver(this);
  }

	public FlatStyle removeFromBottom()
	{
    FlatStyle style = stack.removeLast();
    applyChangesFromBottom(style);
    style.removeObserver(this);
    return style;
	}

  public void styleChanged(int key)
  {
    changes[key] = true;
  }

  private void applyChangesFromBottom(FlatStyle style)
  {
    for(int i = 0; i < Style.STYLE_COUNT; i++)
    {
      String value = get(i);
      if(value == null && style.get(i) != null)
        changes[i] = true;
    }
  }
  
  private void applyChangesFromTop(FlatStyle style)
  {
    for(int i = 0; i < Style.STYLE_COUNT; i++)
    {
      String value = style.get(i);
      if(value != null && !value.equals(get(i)))
        changes[i] = true;
    }
  }
}

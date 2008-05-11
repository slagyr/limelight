package limelight.ui;

import limelight.util.Util;

import java.util.LinkedList;

public class FlatStyle extends Style
{
  private String[] styles;
  private LinkedList<StyleObserver> observers;

  public FlatStyle()
	{
    styles = new String[STYLE_COUNT];
    observers = new LinkedList<StyleObserver>();
  }

  protected String get(int key)
	{
		return styles[key];
	}

	public void put(StyleDescriptor descriptor, String value)
	{
    if(value == null)
      return;
    value = value.trim();
    if(value.length() == 0)
      value = null;

    String originalValue = styles[descriptor.index];
    styles[descriptor.index] = value;
    if(!Util.equal(originalValue, value))
    {
      changes[descriptor.index] = true;
      notifyObservers(descriptor);
    }
  }

  protected boolean has(int key)
  {
    return styles[key] != null && styles[key].length() > 0;
  }

  public String[] getStyles()
  {
    return styles;
  }

  public void addObserver(StyleObserver observer)
  {
    observers.add(observer);
  }

  private void notifyObservers(StyleDescriptor descriptor)
  {
    for(StyleObserver observer : observers)
      observer.styleChanged(descriptor);
  }

  public void removeObserver(StyleObserver observer)
  {
    observers.remove(observer);
  }
}

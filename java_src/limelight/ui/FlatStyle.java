package limelight.ui;

import limelight.Util;

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

	protected void put(int key, String value)
	{
    if(value == null)
      return;
    value = value.trim();
    if(value.length() == 0)
      value = null;

    String originalValue = styles[key];
    styles[key] = value;
    if(!Util.equal(originalValue, value))
    {
      changes[key] = true;
      notifyObservers(key);
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

  private void notifyObservers(int key)
  {
    for(StyleObserver observer : observers)
      observer.styleChanged(key);
  }

  public void removeObserver(StyleObserver observer)
  {
    observers.remove(observer);
  }
}

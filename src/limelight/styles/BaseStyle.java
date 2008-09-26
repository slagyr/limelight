package limelight.styles;

import java.util.LinkedList;

public abstract class BaseStyle extends Style
{
  private LinkedList<StyleObserver> observers;
  protected boolean[] changes;
  private String[] defaults;

  public BaseStyle()
  {
    changes = new boolean[Style.STYLE_COUNT];
  }

  public void setDefault(StyleDescriptor descriptor, String value)
  {
    if(defaults == null)
      defaults = new String[Style.STYLE_COUNT];
    defaults[descriptor.index] = value;
    if(get(descriptor.index) == null && value != null && !value.equals(descriptor.defaultValue))
      recordChange(descriptor, value);
  }

  protected String getDefaultValue(StyleDescriptor descriptor)
  {
    if(defaults != null)
    {
      String value = defaults[descriptor.index];
      if(value != null)
        return value;
    }
    return descriptor.defaultValue;
  }

  protected void recordChange(StyleDescriptor descriptor, String value)
  {
    changes[descriptor.index] = true;
    notifyObserversOfChange(descriptor, value);
  }

  public boolean changed()
  {
    for(int i = 0; i < changes.length; i++)
    {
      if(changes[i])
        return true;
    }
    return false;
  }

  public boolean changed(StyleDescriptor descriptor)
  {
    return changes[descriptor.index];
  }

  public void flushChanges()
  {
    for (int i = 0; i < changes.length; i++)
      changes[i] = false;
  }

  public int getChangedCount()
  {
    int count = 0;
    for (int i = 0; i < changes.length; i++)
      if(changes[i])
        count++;
    return count;
  }

  public void removeObserver(StyleObserver observer)
  {
    if(observers != null)
      observers.remove(observer);
  }

  public void addObserver(StyleObserver observer)
  {
    if(observers == null)
      observers = new LinkedList<StyleObserver>();
    observers.add(observer);
  }

  protected void notifyObserversOfChange(StyleDescriptor descriptor, String value)
  {
    if(observers != null)
    {
      for(StyleObserver observer : observers)
        observer.styleChanged(descriptor, value);
    }
  }

  public boolean hasObserver(StyleObserver observer)
  {
    return observers != null && observers.contains(observer);
  }
}

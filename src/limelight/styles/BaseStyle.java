package limelight.styles;

import limelight.styles.abstrstyling.StyleAttribute;

import java.util.LinkedList;

public abstract class BaseStyle extends Style
{
  private LinkedList<StyleObserver> observers;
  private StyleAttribute[] defaults;

  public void setDefault(StyleDescriptor descriptor, Object value)
  {
    if(defaults == null)
      defaults = new StyleAttribute[Style.STYLE_COUNT];

    StyleAttribute compiledValue = descriptor.compile(value);
    defaults[descriptor.index] = compiledValue;
    if(get(descriptor.index) == null && value != null && !compiledValue.equals(descriptor.defaultValue))
      recordChange(descriptor, compiledValue);
  }

  protected StyleAttribute getDefaultValue(StyleDescriptor descriptor)
  {
    if(defaults != null)
    {
      StyleAttribute value = defaults[descriptor.index];
      if(value != null)
        return value;
    }
    return descriptor.defaultValue;
  }

  protected void recordChange(StyleDescriptor descriptor, StyleAttribute value)
  {
    notifyObserversOfChange(descriptor, value);
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

  protected void notifyObserversOfChange(StyleDescriptor descriptor, StyleAttribute value)
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

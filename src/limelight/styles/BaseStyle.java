//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseStyle extends Style
{
  private final LinkedList<StyleObserver> observers = new LinkedList<StyleObserver>();
  private List<StyleObserver> readonlyObservers;
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
    synchronized(observers)
    {
      observers.remove(observer);
      readonlyObservers = null;
    }
  }

  public void addObserver(StyleObserver observer)
  {
    synchronized(observers)
    {
      observers.add(observer);
      readonlyObservers = null;
    }
  }

  public boolean hasObserver(StyleObserver observer)
  {
    return getObservers().contains(observer);
  }

  public List<StyleObserver> getObservers()
  {
    if(readonlyObservers == null)
    {
      synchronized(observers)
      {
        readonlyObservers = Collections.unmodifiableList(new ArrayList<StyleObserver>(observers));
      }
    }
    return readonlyObservers;
  }

  protected void notifyObserversOfChange(StyleDescriptor descriptor, StyleAttribute value)
  {
    for(StyleObserver observer : getObservers())
      observer.styleChanged(descriptor, value);
  }
}

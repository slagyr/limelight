//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.util.Util;
import limelight.styles.abstrstyling.StyleAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RichStyle extends Style implements StyleObserver
{
  private final StyleAttribute[] styles;
  private final LinkedList<RichStyle> extensions = new LinkedList<RichStyle>();

  public RichStyle()
  {
    styles = new StyleAttribute[STYLE_COUNT];
  }

  public StyleAttribute get(int key)
  {
    if(styles[key] != null)
      return styles[key];

    synchronized(extensions)
    {
      return getFrom(extensions, key);
    }
  }

  public void put(StyleDescriptor descriptor, Object value)
  {
    if(value == null)
      return;
    StyleAttribute compiledValue = descriptor.compile(value);
    putCompiled(descriptor, compiledValue);
  }

  @Override
  protected void putCompiled(StyleDescriptor descriptor, StyleAttribute compiledValue)
  {
    StyleAttribute originalValue = styles[descriptor.index];
    if(!Util.equal(originalValue, compiledValue))
    {
      styles[descriptor.index] = compiledValue;
      notifyObserversOfChange(descriptor, compiledValue);
    }
  }

  public void styleChanged(StyleDescriptor descriptor, StyleAttribute value)
  {
    if(styles[descriptor.index] == null)
      notifyObserversOfChange(descriptor, value);
  }

  public void addExtension(RichStyle extension)
  {
    if(extension != null && !hasExtension(extension))
    {
      applyChangesFromExtension(extension);
      synchronized(extensions)
      {
        extensions.add(extension);
      }
      if(!extension.hasObserver(this))
        extension.addObserver(this);
    }
  }

  public RichStyle getExtention(int index)
  {
    synchronized(extensions)
    {
      return extensions.get(index);
    }
  }

  public boolean hasExtension(Style style)
  {
    synchronized(extensions)
    {
      return extensions.contains(style);
    }
  }

  public void removeExtension(RichStyle extension)
  {
    extension.removeObserver(this);
    applyChangesFromExtension(extension);
    synchronized(extensions)
    {
      extensions.remove(extension);
    }
  }

  public void clearExtensions()
  {
    while(!extensions.isEmpty())
    {
      RichStyle extension = extensions.getFirst();
      removeExtension(extension);
    }
  }

  public void tearDown()
  {
    List<RichStyle> copy;
    synchronized(extensions)
    {
      copy = new ArrayList<RichStyle>(extensions);
      extensions.clear();
    }
    for(RichStyle extension : copy)
    {
      extension.removeObserver(this);
    }
    super.tearDown();
  }

  private void applyChangesFromExtension(RichStyle style)
  {
    LinkedList<RichStyle> seniorExtensions = findSeniorExtensions(style);

    for(StyleDescriptor descriptor : STYLE_LIST)
    {
      StyleAttribute value = style.get(descriptor.index);
      if(value != null && getFrom(seniorExtensions, descriptor.index) == null)
        styleChanged(descriptor, value);
    }
  }

  private LinkedList<RichStyle> findSeniorExtensions(RichStyle style)
  {
    LinkedList<RichStyle> seniorExtensions = new LinkedList<RichStyle>();
    synchronized(extensions)
    {
      for(RichStyle extension : extensions)
      {
        if(extension == style)
          break;
        seniorExtensions.add(extension);
      }
    }
    return seniorExtensions;
  }

  private StyleAttribute getFrom(LinkedList<RichStyle> extensions, int key)
  {
    for(Style style : extensions)
    {
      StyleAttribute value = style.get(key);
      if(value != null)
        return value;
    }
    return null;
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer(super.toString());
    for(int i = 0; i < styles.length; i++)
    {
      StyleAttribute style = styles[i];
      if(style != null)
      {
        StyleDescriptor descriptor = Style.STYLE_LIST.get(i);
        buffer.append("\n\t").append(descriptor.name).append(": ").append(style);
      }
    }
    return buffer.toString();
  }

  public List<RichStyle> getExtentions()
  {
    return Collections.unmodifiableList(new ArrayList<RichStyle>(extensions));
  }
}

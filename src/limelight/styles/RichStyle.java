//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleValue;
import limelight.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RichStyle extends Style implements StyleObserver
{
  private final StyleValue[] styles;
  private final LinkedList<RichStyle> extensions = new LinkedList<RichStyle>();

  public RichStyle()
  {
    styles = new StyleValue[STYLE_COUNT];
  }

  public StyleValue get(int key)
  {
    if(styles[key] != null)
      return styles[key];

    synchronized(extensions)
    {
      return getFrom(extensions, key);
    }
  }

  public void put(StyleAttribute attribute, Object value)
  {
    if(value == null)
      return;
    StyleValue compiledValue = attribute.compile(value);
    putCompiled(attribute, compiledValue);
  }

  @Override
  protected void putCompiled(StyleAttribute attribute, StyleValue compiledValue)
  {
    StyleValue originalValue = styles[attribute.index];
    if(!Util.equal(originalValue, compiledValue))
    {
      styles[attribute.index] = compiledValue;
      notifyObserversOfChange(attribute, compiledValue);
    }
  }

  public void styleChanged(StyleAttribute attribute, StyleValue value)
  {
    if(styles[attribute.index] == null)
      notifyObserversOfChange(attribute, value);
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

    for(StyleAttribute attribute : STYLE_LIST)
    {
      StyleValue value = style.get(attribute.index);
      if(value != null && getFrom(seniorExtensions, attribute.index) == null)
        styleChanged(attribute, value);
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

  private StyleValue getFrom(LinkedList<RichStyle> extensions, int key)
  {
    for(Style style : extensions)
    {
      StyleValue value = style.get(key);
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
      StyleValue style = styles[i];
      if(style != null)
      {
        StyleAttribute attribute = Style.STYLE_LIST.get(i);
        buffer.append("\n\t").append(attribute.name).append(": ").append(style);
      }
    }
    return buffer.toString();
  }

  public List<RichStyle> getExtentions()
  {
    return Collections.unmodifiableList(new ArrayList<RichStyle>(extensions));
  }
}

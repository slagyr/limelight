//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.util.Util;
import limelight.styles.abstrstyling.StyleAttribute;

import java.util.LinkedList;

public class RichStyle extends BaseStyle implements StyleObserver
{
  private final StyleAttribute[] styles;
  private LinkedList<RichStyle> extensions;

  public RichStyle()
  {
    styles = new StyleAttribute[STYLE_COUNT];
  }

  public StyleAttribute get(int key)
  {
    if(styles[key] != null)
      return styles[key];
    else if(extensions != null)
    {
      return getFrom(extensions, key);
    }

    return null;
  }

  public void put(StyleDescriptor descriptor, Object value)
  {
    if(value == null)
      return;

    StyleAttribute originalValue = styles[descriptor.index];
    StyleAttribute compiledValue = descriptor.compile(value);
    styles[descriptor.index] = compiledValue;
    if(!Util.equal(originalValue, compiledValue))
    {
      styles[descriptor.index] = compiledValue;
      notifyObserversOfChange(descriptor, compiledValue);
    }
  }

  public void removeExtension(RichStyle extension)
  {
    if(extensions != null)
    {
      extension.removeObserver(this);
      applyChangesFromExtension(extension);
      extensions.remove(extension);
    }
  }

  public void addExtension(RichStyle extension)
  {
    if(extensions == null)
      extensions = new LinkedList<RichStyle>();

    if(extension != null && !hasExtension(extension))
    {
      applyChangesFromExtension(extension);
      extensions.add(extension);
      if(!extension.hasObserver(this))
        extension.addObserver(this);
    }
  }

  public void clearExtensions()
  {
    while(extensions != null && !extensions.isEmpty())
    {
      RichStyle extension = extensions.getFirst();
      removeExtension(extension);
    }
  }

  public void styleChanged(StyleDescriptor descriptor, StyleAttribute value)
  {
    if(styles[descriptor.index] == null)
      notifyObserversOfChange(descriptor, value);
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
    for(RichStyle extension : extensions)
    {
      if(extension == style)
        break;
      seniorExtensions.add(extension);
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

  public boolean hasExtension(RichStyle style)
  {
    return extensions != null && extensions.contains(style);
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
}

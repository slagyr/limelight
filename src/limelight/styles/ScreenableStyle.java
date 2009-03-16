//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleAttribute;

public class ScreenableStyle extends RichStyle
{
  private Style screen;

  public StyleAttribute get(int key)
  {
    if(screen != null)
    {
      StyleAttribute value = screen.get(key);
      if(value != null)
        return value;
    }

    return super.get(key);
  }

  public Style getScreen()
  {
    return screen;
  }

  public void applyScreen(Style style)
  {
    if(screen != null)
      throw new RuntimeException("Screen already applied");
    applyChangesFromScreen(style, true);
    screen = style;
  }

  private void applyChangesFromScreen(Style screen, boolean in)
  {
    for(StyleDescriptor descriptor : STYLE_LIST)
    {
      StyleAttribute value = screen.get(descriptor.index);
      StyleAttribute originalValue = getCompiled(descriptor);
      if(value != null && !value.equals(originalValue))
      {
        if(in)
          notifyObserversOfChange(descriptor, value);
        else
          notifyObserversOfChange(descriptor, originalValue);
      }
    }
  }

  public void removeScreen()
  {
    if(screen == null)
      return;
    Style screen = this.screen;
    this.screen = null;
    applyChangesFromScreen(screen, false);
  }
}

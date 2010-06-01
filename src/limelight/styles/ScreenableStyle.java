//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.StyleValue;

public class ScreenableStyle extends RichStyle
{
  private Style screen;

  public StyleValue get(int key)
  {
    if(screen != null)
    {
      StyleValue value = screen.get(key);
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
    for(StyleAttribute attribute : STYLE_LIST)
    {
      StyleValue value = screen.get(attribute.index);
      StyleValue originalValue = getCompiled(attribute);
      if(value != null && !value.equals(originalValue))
      {
        if(in)
          notifyObserversOfChange(attribute, value);
        else
          notifyObserversOfChange(attribute, originalValue);
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

  public boolean hasScreen()
  {
    return screen != null;
  }
}

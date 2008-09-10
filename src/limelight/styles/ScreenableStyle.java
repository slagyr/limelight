//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

public class ScreenableStyle extends RichStyle
{
  private Style screen;

  public String get(int key)
  {
    if(screen != null)
    {
      String value = screen.get(key);
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
      String value = screen.get(descriptor.index);
      String originalValue = get(descriptor);
      if(value != null && !value.equals(originalValue))
      {
        changes[descriptor.index] = true;
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

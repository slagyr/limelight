package limelight.ui;

import java.util.Iterator;

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

  public void applyScreen(RichStyle style)
  {
    if(screen != null)
      throw new RuntimeException("Screen already applied");
    applyChangesFromScreen(style);
    screen = style;
  }

  private void applyChangesFromScreen(Style screen)
  {
    for(StyleDescriptor descriptor : Style.STYLE_LIST)
    {
      String value = screen.get(descriptor.index);
      if(value != null && !value.equals(get(descriptor.index)))
        changes[descriptor.index] = true;
    }
  }

  public void removeScreen()
  {
    Style screen = this.screen;
    this.screen = null;
    applyChangesFromScreen(screen);
  }
}

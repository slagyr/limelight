package limelight;

import java.awt.*;

public class MyKeyboardFocusManager extends DefaultKeyboardFocusManager
{
  public void focusComponent(Component c)
  {
    this.setGlobalFocusOwner(c);
  }
}

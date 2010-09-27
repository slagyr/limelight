package limelight.ui.events.panel;

import limelight.ui.Panel;

public class KeyReleasedEvent extends KeyEvent
{
  public KeyReleasedEvent(int modifiers, int keyCode, int keyLocation)
  {
    super(modifiers, keyCode, keyLocation);
  }
}

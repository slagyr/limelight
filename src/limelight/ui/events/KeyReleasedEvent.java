package limelight.ui.events;

import limelight.ui.Panel;

public class KeyReleasedEvent extends KeyEvent
{
  public KeyReleasedEvent(Panel panel, int modifiers, int keyCode, int keyLocation)
  {
    super(panel, modifiers, keyCode, keyLocation);
  }
}

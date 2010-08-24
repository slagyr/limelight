package limelight.ui.events;

import limelight.ui.Panel;

public class KeyPressedEvent extends KeyEvent
{
  public KeyPressedEvent(Panel panel, int modifiers, int keyCode, int keyLocation)
  {
    super(panel, modifiers, keyCode, keyLocation);
  }
}

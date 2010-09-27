package limelight.ui.events.panel;

import limelight.ui.Panel;

public class KeyPressedEvent extends KeyEvent
{
  public KeyPressedEvent(int modifiers, int keyCode, int keyLocation)
  {
    super(modifiers, keyCode, keyLocation);
  }
}

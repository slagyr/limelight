package limelight.ui.events.panel;

import java.awt.*;

public class MouseExitedEvent extends MouseEvent
{
  public MouseExitedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }

  @Override
  public boolean isInheritable()
  {
    return false;
  }
}

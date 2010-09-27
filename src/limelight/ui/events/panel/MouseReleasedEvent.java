package limelight.ui.events.panel;

import java.awt.*;

public class MouseReleasedEvent extends MouseEvent
{
  public MouseReleasedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }
}

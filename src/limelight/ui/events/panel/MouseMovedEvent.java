package limelight.ui.events.panel;

import limelight.ui.Panel;

import java.awt.*;

public class MouseMovedEvent extends MouseEvent
{
  public MouseMovedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }
}

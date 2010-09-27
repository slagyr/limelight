package limelight.ui.events.panel;

import limelight.ui.Panel;

import java.awt.*;

public class MousePressedEvent extends MouseEvent
{
  public MousePressedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }
}

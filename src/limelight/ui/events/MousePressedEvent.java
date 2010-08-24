package limelight.ui.events;

import limelight.ui.Panel;

import java.awt.*;

public class MousePressedEvent extends MouseEvent
{
  public MousePressedEvent(Panel source, int modifiers, Point location, int clickCount)
  {
    super(source, modifiers, location, clickCount);
  }
}

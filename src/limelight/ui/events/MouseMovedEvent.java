package limelight.ui.events;

import limelight.ui.Panel;

import java.awt.*;

public class MouseMovedEvent extends MouseEvent
{
  public MouseMovedEvent(Panel source, int modifiers, Point location, int clickCount)
  {
    super(source, modifiers, location, clickCount);
  }
}

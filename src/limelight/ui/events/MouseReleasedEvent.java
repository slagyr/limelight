package limelight.ui.events;

import limelight.ui.Panel;

import java.awt.*;

public class MouseReleasedEvent extends MouseEvent
{
  public MouseReleasedEvent(Panel source, int modifiers, Point location, int clickCount)
  {
    super(source, modifiers, location, clickCount);
  }
}

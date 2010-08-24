package limelight.ui.events;

import limelight.ui.Panel;

import java.awt.*;

public class MouseDraggedEvent extends MouseEvent
{
  public MouseDraggedEvent(Panel source, int modifiers, Point location, int clickCount)
  {
    super(source, modifiers, location, clickCount);
  }
}

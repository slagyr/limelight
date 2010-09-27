package limelight.ui.events.panel;

import limelight.ui.Panel;

import java.awt.*;

public class MouseDraggedEvent extends MouseEvent
{
  public MouseDraggedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }
}

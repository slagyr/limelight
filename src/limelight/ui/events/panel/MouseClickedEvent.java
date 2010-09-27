package limelight.ui.events.panel;

import java.awt.*;

public class MouseClickedEvent extends MouseEvent
{
  public MouseClickedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }
}

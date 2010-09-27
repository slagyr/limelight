package limelight.ui.events.panel;

import limelight.ui.Panel;

import java.awt.*;

public class MouseEnteredEvent extends MouseEvent
{
  public MouseEnteredEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }

  @Override
  public boolean isInheritable()
  {
    return false;
  }
}

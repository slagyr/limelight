package limelight.ui.events;

import limelight.ui.Panel;

import java.awt.*;

public class MouseExitedEvent extends MouseEvent
{
  public MouseExitedEvent(Panel source, int modifiers, Point location, int clickCount)
  {
    super(source, modifiers, location, clickCount);
  }

  @Override
  public boolean isInheritable()
  {
    return false;
  }
}

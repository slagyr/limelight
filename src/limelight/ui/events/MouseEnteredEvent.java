package limelight.ui.events;

import limelight.ui.Panel;

import java.awt.*;

public class MouseEnteredEvent extends MouseEvent
{
  public MouseEnteredEvent(Panel source, int modifiers, Point location, int clickCount)
  {
    super(source, modifiers, location, clickCount);
  }

  @Override
  public boolean isInheritable()
  {
    return false;
  }
}

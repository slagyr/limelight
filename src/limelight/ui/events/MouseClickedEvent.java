package limelight.ui.events;

import limelight.ui.Panel;
import sun.rmi.server.Dispatcher;

import java.awt.*;

public class MouseClickedEvent extends MouseEvent
{
  public MouseClickedEvent(Panel source, int modifiers, Point location, int clickCount)
  {
    super(source, modifiers, location, clickCount);
  }
}

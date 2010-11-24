//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.events.panel;

import java.awt.*;

public class MouseExitedEvent extends MouseEvent
{
  public MouseExitedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }

  @Override
  public boolean isInheritable()
  {
    return false;
  }
}

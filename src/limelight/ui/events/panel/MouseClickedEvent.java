//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.events.panel;

import java.awt.*;

public class MouseClickedEvent extends MouseEvent
{
  public MouseClickedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }
}

//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.events.panel;

import java.awt.*;

public class MousePressedEvent extends MouseEvent
{
  public MousePressedEvent(int modifiers, Point location, int clickCount)
  {
    super(modifiers, location, clickCount);
  }
}

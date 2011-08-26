//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.events.panel;

import java.awt.*;

public class MouseWheelEvent extends MouseEvent
{
  public static final int UNIT_SCROLL = 0;
  public static final int BLOCK_SCROLL = 1;

  private int scrollType;
  private int scrollAmount;
  private int wheelRotation;

  public MouseWheelEvent(int modifiers, Point location, int clickCount, int scrollType, int scrollAmount, int wheelRotation)
  {
    super(modifiers, location, clickCount);
    this.scrollType = scrollType;
    this.scrollAmount = scrollAmount;
    this.wheelRotation = wheelRotation;
  }

  public int getScrollType()
  {
    return scrollType;
  }

  public int getScrollAmount()
  {
    return scrollAmount;
  }

  public int getWheelRotation()
  {
    return wheelRotation;
  }

  public boolean isVertical()
  {
    return (getModifiers() & SHIFT_MASK) == 0;
  }

  public boolean isHorizontal()
  {
    return (getModifiers() & SHIFT_MASK) != 0;
  }

  public int getUnitsToScroll()
  {
    return scrollAmount * wheelRotation;
  }

  @Override
  public String toString()
  {
    return super.toString() + " scrollType=" + scrollType + " scrollAmount=" + scrollAmount + " wheelRotation=" + wheelRotation;
  }
}

//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.XCoordinateValue;

import java.awt.*;

public class StaticXCoordinateValue extends SimpleIntegerValue implements XCoordinateValue
{
  public StaticXCoordinateValue(int value)
  {
    super(value);
  }

  public int getX(int consumed, Rectangle area)
  {
    return getValue() + area.x;
  }
}

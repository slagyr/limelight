//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.YCoordinateValue;

import java.awt.*;

public class StaticYCoordinateValue extends SimpleIntegerValue implements YCoordinateValue
{
  public StaticYCoordinateValue(int value)
  {
    super(value);
  }

  public int getY(int consumed, Rectangle area)
  {
    return getValue() + area.y;
  }
}

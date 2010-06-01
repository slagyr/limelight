//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.YCoordinateValue;

import java.awt.*;

public class PercentageYCoordinateValue extends SimplePercentageValue implements YCoordinateValue
{
  public PercentageYCoordinateValue(double percentage)
  {
    super(percentage);
  }

  public int getY(int consumed, Rectangle area)
  {

    return (int) ((getPercentage() * 0.01) * (double) area.height) + area.y;
  }
}

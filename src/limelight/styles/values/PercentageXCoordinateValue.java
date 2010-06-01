//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.XCoordinateValue;

import java.awt.*;

public class PercentageXCoordinateValue extends SimplePercentageValue implements XCoordinateValue
{
  public PercentageXCoordinateValue(double percentage)
  {
    super(percentage);
  }

  public int getX(int consumed, Rectangle area)
  {

    return (int) ((getPercentage() * 0.01) * (double) area.width) + area.x;
  }
}

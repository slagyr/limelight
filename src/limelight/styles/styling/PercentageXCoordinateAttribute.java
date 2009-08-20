//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.XCoordinateAttribute;

import java.awt.*;

public class PercentageXCoordinateAttribute extends SimplePercentageAttribute implements XCoordinateAttribute
{
  public PercentageXCoordinateAttribute(double percentage)
  {
    super(percentage);
  }

  public int getX(int consumed, Rectangle area)
  {

    return (int) ((getPercentage() * 0.01) * (double) area.width) + area.x;
  }
}

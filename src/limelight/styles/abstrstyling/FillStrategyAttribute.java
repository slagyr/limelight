//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.ui.painting.ImageFillStrategy;

import java.awt.*;

public abstract class FillStrategyAttribute implements StyleAttribute
{
  public abstract String name();
  public abstract void fill(Graphics2D graphics, Image image, XCoordinateAttribute xCoordinate, YCoordinateAttribute yCoordinate);

  protected int numberOfPaints(int size, int max)
  {
    int paints = max / size;
    if(max % size > 0)
      return paints + 1;
    else return paints;
  }

  public String toString()
  {
    return name();
  }
}
